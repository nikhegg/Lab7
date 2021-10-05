package utils;
import java.net.DatagramPacket;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HostAnalyzer extends HostModule {
    private ForkJoinPool multithreader;

    public HostAnalyzer(Host host) {
        super(host);
        this.multithreader = new ForkJoinPool();
    }

    public void addAnalysisTask(DatagramPacket packet) {
        if(!this.isActive()) return;
        class AnalysisTask extends RecursiveTask<Boolean> {
            private HostAnalyzer caller;

            AnalysisTask(HostAnalyzer caller) {
                this.caller = caller;
            }

            @Override
            protected Boolean compute() {
                Request incomingRequest = this.caller.getHost().getSerializer().deserialize(packet.getData());
                incomingRequest.setAuthor(packet.getAddress().toString());
                // Check user
                if(!this.caller.getHost().getAccountLord().isValidAccount(incomingRequest.getAccount().getName(), incomingRequest.getAccount().getPassword())) {
                    // Reply that account data is incorrect by Sender
                    this.caller.getHost().getSender().sendErrorPacket("?pass", packet.getAddress(), packet.getPort());
                    return true;  
                } else if(this.caller.getHost().getAccountLord().isOnline(incomingRequest.getAccount().getName()) && incomingRequest.getMessage().startsWith("@connect")) {
                    // Reply that account is already logged in
                    this.caller.getHost().getSender().sendErrorPacket("?online", packet.getAddress(), packet.getPort());
                } else { // Everything is okay
                    if(!this.caller.getHost().isConnected(packet.getAddress().toString())) { // First connection
                        try {
                            this.caller.getHost().getAccountLord().logInUser(incomingRequest.getAccount().getName(), incomingRequest.getAccount().getPassword());
                            this.caller.getHost().addConnection(packet.getAddress().toString());
                            this.caller.getHost().getSender().sendInitPacket(packet.getAddress(), packet.getPort());
                            System.out.printf("\n[SERVER]: New connection was established (by %s as @%s)\n", packet.getAddress().toString(), incomingRequest.getAccount().getName());
                        } catch(Exception e) {
                            // Reply the exepcetion error to the client by Sender (Incorrect data or active session)
                            this.caller.getHost().getSender().sendErrorPacket("?online", packet.getAddress(), packet.getPort());
                            return true;
                        }
                    } else {
                        String received = incomingRequest.getMessage();
                        if(received.startsWith("@reconnect")) {
                            // Check account changes
                            System.out.printf("\n[SERVER]: %s lost connection and reconnected again\n", packet.getAddress());
                        } else if(received.startsWith("exit")) {
                            this.caller.getHost().removeConnection(packet.getAddress().toString());
                            System.out.printf("\n[SERVER]: %s disconnected\n", packet.getAddress());
                            try {
                                this.caller.getHost().getAccountLord().logOutUser(incomingRequest.getAccount().getName(), incomingRequest.getAccount().getPassword());
                            } catch(Exception e) {
                                // Just ignore, nothing to reply
                            }
                            
                        } else {
                            System.out.printf("[%s@%s]: %s\n", incomingRequest.getAccount().getName(), packet.getAddress(), incomingRequest.getMessage());
                            String requestResult = this.caller.getHost().getConsoler().doHostRequest(incomingRequest);
                            System.out.println(requestResult);
                            this.caller.getHost().getSender().sendDefaultPacket(requestResult, packet.getAddress(), packet.getPort());
                        }
                    }
                }
                return true;
            }
        }

        this.multithreader.invoke(new AnalysisTask(this));
        return;
    }
}
