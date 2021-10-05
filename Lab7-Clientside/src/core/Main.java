package core;
import utils.AccountManager;
import utils.Connector;

public class Main {
    private static final String defaultAddress = "localhost";
    private static final int defaultPort = 2228;
    private static final long maxPing = 999;
    private static final int bufferSize = 8192;

    public static void main(String[] args) {
        AccountManager accManager = new AccountManager();
        boolean done = accManager.requireNewAccount();
        if(!done) return;
        Connector connector = new Connector(defaultAddress, defaultPort, maxPing, bufferSize, accManager);
        connector.activate();
    }
}