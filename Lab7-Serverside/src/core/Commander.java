package core;
import commands.AbCommand;
import misc.Route;
import utils.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class Commander implements Serializable {
    private HashMap<String, AbCommand> cmds = new HashMap<>();
    private Consoler consoler;

    /**
     * @param commands
     */
    public Commander(AbCommand... commands) {
        for(AbCommand cmd: commands) {
            this.cmds.put(cmd.getName(), cmd);
        }
    }

    /**
     * @param command
     */
    public void addCmd(AbCommand command) {
        this.cmds.put(command.getName(), command);
    }

    /**
     * @param command
     */
    public String sendCmd(String command) {
        command = command.trim();
        String[] preargs = command.split(" ", 2);
        if(preargs.length >= 2) preargs[1] = preargs[1].trim();
        String[] args = Arrays.copyOfRange(preargs, 1, command.split(" ").length);
        String cmd = command.split(" ")[0].trim();
        switch(cmd) {
            case "help":
                cmds.get(cmd).execute(this.cmds, args, this.consoler.getMode());
                return "";
            default:
                try {
                    String result = cmds.get(cmd).execute(args, this.consoler.getMode(), new User("console", "console"));
                    return result;
                } catch(Exception e) {
                    return "Unknown command. Type \"help\" to get the list of commands";
                }
        }
    }

    public String sendObjectCmd(String command, Route route, User user) {
        command = command.trim();
        String[] preargs = command.split(" ", 2);
        if(preargs.length >= 2) preargs[1] = preargs[1].trim();
        String[] args = Arrays.copyOfRange(preargs, 1, command.split(" ").length);
        String cmd = command.split(" ")[0].trim();
        try {
            String result = cmds.get(cmd).execute(args, this.consoler.getMode(), route);
            return result;
        } catch(Exception e) {
            return "Unknown command. Type \"help\" to get the list of commands";
        }
    }

    public HashMap<String, AbCommand> getCommands() {
        return this.cmds;
    }
    
    public void bindConsoler(Consoler consoler) {
        this.consoler = consoler;
    }
}
