package utils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HostReciever extends HostModule {
    private ForkJoinPool multithreader;

    public HostReciever(Host host) {
        super(host);
        this.multithreader = new ForkJoinPool();
    }

    private boolean getRequest() {
        if(!this.isActive()) return false;

        class RecieveTask extends RecursiveTask<Boolean> {
            private HostReciever caller;

            RecieveTask(HostReciever caller) {
                this.caller = caller;
            }

            protected Boolean compute() {
                byte[] buffer = new byte[this.caller.getHost().getBufferSize()];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    this.caller.getHost().getSocket().receive(packet);
                } catch(IOException e) {
                    return false;
                }
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                this.caller.getHost().triggerAnalyzer(packet);
                return true;
            }
        }
        this.multithreader.invoke(new RecieveTask(this));
        return true;
    }

    @Override
    public void activate() {
        if(!this.isActive()) this.changeActive();
        while(this.isActive()) {
            boolean done = getRequest();
        }
    }

    public void deactivate() {
        if(this.isActive()) this.changeActive();
    }

}
