package utils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class ClientInteractor {
    private Connector connector;
    private String address;
    private int port;
    private int bufferSize;
    private long maxPing;
    private DatagramChannel channel;

    ClientInteractor(Connector connector, String address, int port, long maxPing, int bufferSize) {
        this.connector = connector;
        this.address = address;
        this.port = port;
        this.maxPing = maxPing;
        this.bufferSize = bufferSize;
    }

    public void send(byte[] buffer) {
        try {
            this.channel = DatagramChannel.open();
            ByteBuffer byteBuffer = ByteBuffer.allocate(this.bufferSize);
            byteBuffer.clear();
            byteBuffer.put(buffer);
            byteBuffer.flip();

            SocketAddress socket = new InetSocketAddress(this.address, this.port);
            this.channel.send(byteBuffer, socket);
        } catch(UnresolvedAddressException e) {

        } catch(IllegalArgumentException e) {
            
        } catch(IOException e) {

        }
    }

    public byte[] recieve() throws TimeoutException {
        /*try {
            if(!this.channel.isOpen()) return null;
            if(this.connector.getRequiredResponses() < 1) return null;
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(this.port + 1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }*/
        try {
            Selector selector = Selector.open();
            this.channel.configureBlocking(false);
            //DatagramSocket socket = channel.socket();
            //socket.bind(new InetSocketAddress(this.port + 1));
            this.channel.register(selector, SelectionKey.OP_READ);
            long waitStarted = System.currentTimeMillis();
            while(true) {
                if(System.currentTimeMillis() - waitStarted > this.maxPing) {
                    throw new TimeoutException("Server timed out");
                }
                if (selector.selectNow() > 0) {
                    Set<SelectionKey> selectionKeys = selector.keys();
                    for (SelectionKey selectionKey : selectionKeys) {
                        if (selectionKey.isReadable()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(this.bufferSize);
                            byteBuffer.clear();
                            channel.receive(byteBuffer);
                            selector.close();
                            return byteBuffer.array();
                        } else {
                            System.out.println("UNREADABLE");
                        }
                    }
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }

    public void close() {
        try {
            this.channel.close();
        } catch(IOException e) {
            return;
        }
    }

    public Connector getConnector() {
        return this.connector;
    }
}
