package utils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import core.Globals;

public class HostSender extends HostModule {
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public HostSender(Host host) {
        super(host);
    }

    private boolean sendPacket(DatagramPacket packet) {
        class SenderTask implements Runnable {
            private HostSender caller;

            SenderTask(HostSender caller) {
                this.caller = caller;
            }

            @Override
            public void run() {
                try {
                    this.caller.getHost().getSocket().send(packet);
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        Future<?> future = fixedThreadPool.submit(new SenderTask(this));
        if(future.isDone()) return true;
        else return false;
    }
    
    public void sendInitPacket(InetAddress address, int port) {
        byte[] initBuffer = this.getHost().getSerializer().serialize(new Request(this.getHost().getCCM().getCommands(), Globals.getRoutesCreated()));
        sendPacket(new DatagramPacket(initBuffer, initBuffer.length, address, port));
    }

    public void sendErrorPacket(String error, InetAddress address, int port) {
        byte[] initBuffer = this.getHost().getSerializer().serialize(new Request(error, Globals.getRoutesCreated()));
        sendPacket(new DatagramPacket(initBuffer, initBuffer.length, address, port));
    }

    public void sendDefaultPacket(String message, InetAddress address, int port) {
        byte[] initBuffer = this.getHost().getSerializer().serialize(new Request(message, Globals.getRoutesCreated()));
        sendPacket(new DatagramPacket(initBuffer, initBuffer.length, address, port));
    }

}
