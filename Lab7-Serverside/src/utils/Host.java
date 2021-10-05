package utils;
import java.io.IOException;
import java.io.Serializable;
import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

import core.Consoler;
import misc.VectorCore;
import utils.database.AccountLord;
import utils.database.DatabaseCollectionManager;
import utils.database.DatabaseManager;
import utils.database.DatabaseUserManager;

public class Host extends Thread implements Serializable {
    private HashMap<String, Boolean> connections;
    private int port;
    private int bufferSize;
    private DatagramSocket socket;
    private ClientCommandManager ccm;
    private HostReciever reciever; // For Lab7
    private HostSender sender; // For Lab7
    private HostAnalyzer analyzer; // For Lab7
    private AccountLord accountLord;
    private VectorCore collection;
    private Consoler consoler;
    private final Serializer serializer = new Serializer();

    private DatabaseManager dm;
    private DatabaseCollectionManager dcm;
    private DatabaseUserManager dum;
    
    public Host(int port, int bufferSize, VectorCore collection) {
        this.connections = new HashMap<String, Boolean>();
        this.port = port;
        this.bufferSize = bufferSize;
        this.reciever = new HostReciever(this); // For Lab7
        this.analyzer = new HostAnalyzer(this);
        this.sender = new HostSender(this); // For Lab7
        this.accountLord = new AccountLord(this);
        this.collection = collection;
        this.consoler = null;
    }

    public void run() {
        try {
            this.socket = new DatagramSocket(port);
            this.analyzer.activate();
            this.sender.activate();
            this.reciever.activate();
            System.out.printf("Started server on the port %s\n", port);    
        } catch (IOException e) {
            if(this.socket.isClosed()) return;
            System.out.printf("Server is already started on this port: %s\n", port);
            return;
        }
    }
    
    public void disable() {
        this.reciever.deactivate();
        this.analyzer.deactivate();
        this.sender.deactivate();
        this.socket.close();
        System.out.println("Server is offline now");
    }

    public void triggerAnalyzer(DatagramPacket packet) {
        this.analyzer.addAnalysisTask(packet);
    }

    public HostSender getSender() {
        return this.sender;
    }

    public HashMap<String, Boolean> getConnections() {
        return this.connections;
    }
    
    public boolean addConnection(String address) {
        if(this.connections.containsKey(address)) return false;
        this.connections.put(address, true);
        return true;
    }

    public boolean removeConnection(String address) {
        if(!this.connections.containsKey(address)) return false;
        this.connections.remove(address);
        return true;
    }

    public boolean isConnected(String address) {
        return this.connections.containsKey(address);
    }

    public int getPort() {
        return this.port;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public AccountLord getAccountLord() {
        return accountLord;
    }

    public void bindConsoler(Consoler consoler) {
        this.consoler = consoler;
        this.ccm = new ClientCommandManager(this.consoler.getCommands());
    }

    public Consoler getConsoler() {
        return this.consoler;
    }

    public VectorCore getCollection() {
        return this.collection;
    }

    public ClientCommandManager getCCM() {
        return this.ccm;
    }

    public Serializer getSerializer() {
        return this.serializer;
    }

    public void connectDatabases(DatabaseManager dm, DatabaseCollectionManager dcm, DatabaseUserManager dum) {
        this.dm = dm;
        this.dcm = dcm;
        this.dum = dum;
    }

    public DatabaseManager getDM() {
        return this.dm;
    }

    public DatabaseCollectionManager getDCM() {
        return this.dcm;
    }

    public DatabaseUserManager getDUM() {
        return this.dum;
    }
}
