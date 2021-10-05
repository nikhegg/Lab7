package utils;
import java.util.HashMap;
import exceptions.NullFieldException;

public class ServerInfoManager {
    private Connector connector;
    private HashMap<String, ClientCommand> commands;
    private CommandVerifier verifier;
    private long routesCreated;
    private boolean requiresSynchronization;

    ServerInfoManager(Connector connector) {
        this.connector = connector;
        reset();
        this.verifier = new CommandVerifier(this);
    }

    private void addSystemCommands() {
        String[] args = {"String"};
        this.commands.put("execute_script", new ClientCommand("execute_script", "Executes the script from a specified file", "execute_script <path>", args));
        this.commands.put("@connect", new ClientCommand("@connect", "Establishes the connection with the server", "@connect", new String[0]));
        this.commands.remove("save");
    }

    public void synchronize(HashMap<String, ClientCommand> commands, long routesCreated) {
        this.commands = commands;
        addSystemCommands();
        this.routesCreated = routesCreated;
        this.requiresSynchronization = false;
    }

    public void reset() {
        this.commands = null;
        this.routesCreated = 0;
        requiresSynchronization = true;
    }

    public void setRoutesCreated(long routesCreated) {
        this.routesCreated = routesCreated;
    }

    public void incRoutesCreated() {
        this.routesCreated += 1;
    }

    public void decRoutesCreated() {
        this.routesCreated -= 1;
    }

    public Connector getConnector() throws NullFieldException{
        if(this.requiresSynchronization) throw new NullFieldException("Client data was not synchronized with server info");
        return this.connector;
    }

    public HashMap<String, ClientCommand> getSynchronizedCommands() throws NullFieldException {
        if(this.requiresSynchronization) throw new NullFieldException("Client data was not synchronized with server info");
        return this.commands;
    }

    public long getRoutesCreated() throws NullFieldException {
        if(this.requiresSynchronization) throw new NullFieldException("Client data was not synchronized with server info");
        return this.routesCreated;
    }

    public CommandVerifier getCommandVerifier() {
        return this.verifier;
    }
}
