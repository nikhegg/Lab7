package core;
import commands.AbCommand;
import commands.ExecuteScript;
import commands.Exit;
import utils.Host;
import utils.Request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Consoler implements Serializable {
    private boolean active;
    private final Commander commander;
    private static Scanner commandReader = new Scanner(System.in);
    private ConsolerMode mode;
    private final Host host;

    /**
     * @param commander
     */
    public Consoler(Commander commander, Host host) {
        this.active = false;
        this.commander = commander;
        this.host = host;
        this.mode = ConsolerMode.USER;
        commander.bindConsoler(this);
        host.bindConsoler(this);
    }

    /**
     *
     */
    public void start() {
        try {
            System.out.println("| Lab6 Project by Nikita Kuldyshev\n" +
                    "| Version: " + Globals.getVersion() + "\n");
            this.active = true;
            this.commander.addCmd(new Exit(this));
            this.commander.addCmd(new ExecuteScript(this));
            System.out.print("> ");
            String cmd = commandReader.nextLine();
            System.out.println(this.commander.sendCmd(cmd));
            System.out.println();
            while(this.active) {
                System.out.print("> ");
                cmd = commandReader.nextLine();
                if(this.mode == ConsolerMode.USER) System.out.println(this.commander.sendCmd(cmd));
            }
        } catch(NoSuchElementException e) {
            this.host.disable();
            System.exit(0);
        } catch (IllegalStateException e) {
            this.host.disable();
            System.exit(0);
        }

    }

    /**
     * @param scannedScript
     * @return
     */
    public boolean doScript(Scanner scannedScript) {
        setMode(ConsolerMode.SCRIPT);
        declareNewScanner(scannedScript);
        String cmd = commandReader.nextLine();
        System.out.println("> " + cmd);
        System.out.println(this.commander.sendCmd(cmd));
        while(this.active && scannedScript.hasNextLine()) {
            cmd = commandReader.nextLine();
            System.out.println("> " + cmd);
            System.out.println(this.commander.sendCmd(cmd));
        }
        restoreScanner();
        setMode(ConsolerMode.USER);
        return true;
    }

    public String doHostRequest(Request request) {
        setMode(ConsolerMode.CLIENT);
        // Is a command available for client?
        String command = request.getCommand().trim();
        if(!this.commander.getCommands().containsKey(command) || (this.commander.getCommands().containsKey(command) && this.commander.getCommands().get(command).isServerOnly())) return "Unknown command. Type \"help\" to get the list of commands";
        String response;
        if((command.equals("add") || command.equals("add_if_min") || command.equals("update")) && request.getRoute() != null) {
            response = this.commander.sendObjectCmd(request.getMessage(), request.getRoute(), request.getAccount());
        } else response = this.commander.sendCmd(request.getMessage());
        setMode(ConsolerMode.USER);
        return response;
    }

    /**
     *
     */
    public void stop() {
        this.active = false;
    }

    /**
     * @param scanner
     */
    public void declareNewScanner(Scanner scanner) {
        commandReader = scanner;
    }

    /**
     *
     */
    public void restoreScanner() {
        commandReader = new Scanner(System.in);
    }

    /**
     * @param mode
     */
    public void setMode(ConsolerMode mode) {
        this.mode = mode;
    }

    /**
     * @return
     */
    public static Scanner getScanner() {
        return commandReader;
    }

    /**
     * @return
     */
    public ConsolerMode getMode() {
        return this.mode;
    }

    public HashMap<String, AbCommand> getCommands() {
        return this.commander.getCommands();
    }

    public Host getHost() {
        return this.host;
    }
}
