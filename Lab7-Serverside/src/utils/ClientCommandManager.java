package utils;

import java.io.Serializable;
import java.util.HashMap;

import commands.AbCommand;

public class ClientCommandManager implements Serializable {
    private final HashMap<String, ClientCommand> commands;

    public ClientCommandManager(HashMap<String, AbCommand> serverCommands) {
        this.commands = new HashMap<String, ClientCommand>();
        serverCommands.forEach((String k, AbCommand v) -> {
            if(!v.isHidden()) {
                String key = k;
                // desc, usage, args[]
                ClientCommand value = new ClientCommand(v.getName(), v.getDescription(), v.getUsage(), v.getArgsRequirements());
                this.commands.put(key, value);
            }
        });
    }

    public HashMap<String, ClientCommand> getCommands() {
        return this.commands;
    }
}
