package utils;
import java.util.Arrays;
import exceptions.NullFieldException;

public class CommandVerifier {
    private ServerInfoManager sim;

    CommandVerifier(ServerInfoManager sim) {
        this.sim = sim;
    }

    public String meetsCommandRequirements(String message) {
        try {
            String command = message.split(" ")[0];
            String[] args = Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length);
            if(!doesExist(command)) return "Unknown command. Type \"help\" to get the list of commands";
            ClientCommand commandInfo = this.sim.getSynchronizedCommands().get(command);
            if(args.length < commandInfo.getArgs().length) return "Command needs " + commandInfo.getArgs().length + " arguments, got " + args.length;
            for(int i = 0; i < commandInfo.getArgs().length; i++) {
                switch(commandInfo.getArgs()[i]) {
                    case "String":
                        break;
                    case "long":
                        try {
                            Long.parseLong(args[i]); 
                        } catch(NumberFormatException e) {
                            return "Argument #" + (i+1) + " should be long";
                        }
                        break;
                    case "int":
                        try {
                            Integer.parseInt(args[i]);
                        } catch(NumberFormatException e) {
                            System.out.println(commandInfo.getArgs()[i]);
                            return "Argument #" + (i+1) + " should be int";
                        }
                        break;
                    default:
                        return "Command has unknown type of argument";
                }
            }
            return null; //if meetsCommandRequirements() result is null then everything is okay
        } catch(NullFieldException e) {
            return e.getMessage();
        }
        
    }

    public boolean doesExist(String commandName) {
        try {
            if(this.sim.getSynchronizedCommands().containsKey(commandName)) return true;
            else return false;
        } catch(NullFieldException e) {
            return false;
        }
    }
}
