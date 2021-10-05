package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import core.RouteMaker;
import exceptions.NullFieldException;
import exceptions.RecursionException;
import misc.Route;

public class Connector {
    private String address;
    private int port;
    private int bufferSize;
    private long maxPing; // Max ping value that allowed during the interaction with server
    private AccountManager accManager;
    private boolean connected; // Is client connected to the server or not
    private boolean active; // Is client running now or not
    private boolean scriptExecution;

    private final Vector<String> usedScripts;
    private final ClientInteractor interactor; // Sends packets to server and recieves packets from it
    private final ServerInfoManager dataManager; // Storages all required information from server (Amount of created routes & all commands)
    private final Serializer serializer = new Serializer();
    private final InputController inputController; // Controls all input and checks whether the input is a command or not
    private final RouteMaker routeMaker; // Creates routes in order to send them to the server

    public Connector(String address, int port, long maxPing, int bufferSize, AccountManager accManager) {
        this.address = address;
        this.port = port;
        this.bufferSize = bufferSize;
        this.maxPing = maxPing;
        this.accManager = accManager;
        this.connected = false;
        this.active = false;
        this.scriptExecution = false;
        usedScripts = new Vector<String>();
        interactor = new ClientInteractor(this, this.address, this.port, this.maxPing, this.bufferSize);
        dataManager = new ServerInfoManager(this);
        inputController = new InputController(this.dataManager.getCommandVerifier());
        routeMaker = new RouteMaker(new Scanner(System.in), dataManager);
    }

    public void changeAddress(String newAddress) {
        String oldAddress = this.address;
        if(newAddress.equals("")) return;
        this.address = newAddress;
        System.out.printf("Address was changed from %s to %s", oldAddress, newAddress);
    }

    public void changePort(int newPort) {
        int oldPort = this.port;
        if(newPort <= 0) return;
        this.port = newPort;
        System.out.printf("Port was changed from %s to %s", oldPort, newPort);
    }

    private Request establishInitConnection() {
        try {
            byte[] recieved = interactor.recieve();
            Request incomingRequest = serializer.deserialize(recieved);
            incomingRequest.setAuthor(this.address);
            if(incomingRequest.getMessage().equals("?user") || incomingRequest.getMessage().equals("?pass")) {
                System.out.println("Invalid login or password");
                this.accManager.requireNewAccount();
                System.out.printf("Trying to establish connection with %s:%s...\n", this.address, this.port);
                Request request = new Request("@connect", 0);
                request.setAccount(this.accManager.getAccount());
                interactor.send(serializer.serialize(request));
                return establishInitConnection();
            } else if(incomingRequest.getMessage().equals("?online")) {
                System.out.println("This account is already online");
                this.accManager.requireNewAccount();
                System.out.printf("Trying to establish connection with %s:%s...\n", this.address, this.port);
                Request request = new Request("@connect", 0);
                request.setAccount(this.accManager.getAccount());
                interactor.send(serializer.serialize(request));
                return establishInitConnection();
            }
            HashMap<String, ClientCommand> commands = incomingRequest.getMap();
            dataManager.synchronize(commands, incomingRequest.getRoutes());
            System.out.printf("Connected to server as @%s\n", this.accManager.getAccount().getName());
            this.connected = true;
            return incomingRequest;
        } catch(TimeoutException e) {
            this.connected = false;
            System.out.printf("[ERROR]: %s. Try to reconnect typing \"@connect\" or start the program again\n", e.getMessage());
        } catch(RuntimeException e) {
            this.connected = false;
            System.out.println("[ERROR]: Not connected to the server. Try to reconnect typing \"@connect\" or start the program again");
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    private void executeCommand(String message) {
        Request outcomingRequest = new Request(message, 0);
        outcomingRequest.setAccount(this.accManager.getAccount());
        Request incomingRequest;
        try {
            if(!message.split(" ")[0].equals("@connect")) outcomingRequest.setRoutes(this.dataManager.getRoutesCreated());
            switch(message.split(" ")[0]) {
                case "help":
                    if(!this.connected) {
                        interactor.send(serializer.serialize(outcomingRequest));
                        return;
                    }
                    System.out.println("All commands:");
                    this.dataManager.getSynchronizedCommands().forEach((String k, ClientCommand v) -> {
                        String info = "• " + v.getName() + ": " + v.getDescription();
                        System.out.println(info);
                    });
                    return;
                case "exit":
                    if(this.connected) interactor.send(serializer.serialize(outcomingRequest));
                    this.active = false;
                    System.out.println("Shutting down the program...");
                    break;
                case "@connect":
                    interactor.send(serializer.serialize(outcomingRequest));
                    this.connected = false;
                    System.out.printf("Trying to reconnect to %s:%s...\n", this.address, this.port);
                    incomingRequest = establishInitConnection();
                    return;
                case "execute_script":
                    outcomingRequest.setAuthor("local");
                    String path = outcomingRequest.getFirstArg();
                    outcomingRequest.setAuthor(null);
                    File file = new File(path);
                    Scanner fsc = new Scanner(file);
                    this.scriptExecution = true;
                    while(fsc.hasNextLine() && this.scriptExecution) {
                        this.scriptExecution = true;
                        String script = fsc.nextLine();
                        System.out.println(script);
                        if(usedScripts.contains(script)) {
                            fsc.close();
                            this.scriptExecution = false;
                            usedScripts.clear();
                            throw new RecursionException("[ERROR]: Script execution stopped - recursion found");
                        }
                        usedScripts.add(script);
                        this.routeMaker.setNewScanner(fsc);
                        executeCommand(script);
                        usedScripts.remove(script);
                    }
                    this.routeMaker.setNewScanner(new Scanner(System.in));
                    this.scriptExecution = false;
                    fsc.close();
                    usedScripts.clear();
                    return;
                /*case "clear":
                    // Add clear submittion and then send packet Request("clear", ...);
                    System.out.println("Are you sure that you want to clear the whole collection?\nThis action cannot be aborted\nType \"Y\" to clear or \"N\" to stop the operation");
                    Scanner scanner = new Scanner(System.in);
                    char locationExistence = scanner.next().trim().charAt(0);
                    scanner.close();
                    switch(locationExistence) {
                        case 'Y':
                        case 'y':
                            outcomingRequest.changeMessage("clear");
                            interactor.send(serializer.serialize(outcomingRequest));
                            incomingRequest = serializer.deserialize(interactor.recieve());
                            if(incomingRequest == null) {
                                System.out.println("Something went wrong. It is recommended to type \"@connect\" to reconnect to the server, but not necessary");
                                return;
                            } else {
                                incomingRequest.setAuthor(this.address);
                                System.out.println(incomingRequest.getMessage() + "\n");
                            }
                            break;
                        case 'N':
                        case 'n':
                            System.out.println("Clearance was aborted");
                            break;
                        default:
                            System.out.println("Unknown symbol. Clearance was aborted");
                            break;
                    }
                    return;*/
                case "add":
                case "add_if_min":
                case "update":
                    // Use RouteMaker here and send object to the server
                    if(!this.scriptExecution) this.routeMaker.setNewScanner(new Scanner(System.in));
                    Route route = this.routeMaker.makeNewRoute(this.scriptExecution, this.accManager.getAccount());
                    Request routeRequest = new Request(message, this.dataManager.getRoutesCreated(), route);
                    interactor.send(serializer.serialize(routeRequest));
                    incomingRequest = serializer.deserialize(interactor.recieve());
                    if(incomingRequest == null) {
                        System.out.println("Something went wrong. It is recommended to type \"@connect\" to reconnect to the server, but not necessary");
                        return;
                    } else {
                        incomingRequest.setAuthor(this.address);
                        System.out.println(incomingRequest.getMessage() + "\n");
                        this.dataManager.setRoutesCreated(incomingRequest.getRoutes());
                    }
                    return;
                default:
                    interactor.send(serializer.serialize(outcomingRequest));
                    incomingRequest = serializer.deserialize(interactor.recieve());
                    if(incomingRequest == null) {
                        System.out.println("Something went wrong. It is recommended to type \"@connect\" to reconnect to the server, but not necessary");
                        return;
                    } else {
                        incomingRequest.setAuthor(this.address);
                        System.out.println(incomingRequest.getMessage() + "\n");
                    }
                    return;
            }
        } catch(FileNotFoundException e) {
            System.out.println("[ERROR]: File not found");
        } catch (NullFieldException e) {
            this.connected = false;
            System.out.printf("[ERROR]: %s. Try to type @connect\n", e.getMessage());
        } catch(TimeoutException e) {
            this.connected = false;
            System.out.printf("[ERROR]: %s. Try to reconnect typing \"@connect\" or start the program again\n", e.getMessage());
        } catch(RuntimeException e) {
            this.connected = false;
            System.out.println("[ERROR]: Not connected to the server. Try to reconnect typing \"@connect\" or start the program again");
        } catch(RecursionException e) {
            this.routeMaker.setNewScanner(new Scanner(System.in));
            this.scriptExecution = false;
            System.out.println(e.getMessage());
        }
    }

    public void activate() {
        this.active = true;
        System.out.printf("Trying to establish connection with %s:%s...\n", this.address, this.port);
        Request request = new Request("@connect", 0);
        request.setAccount(this.accManager.getAccount());
        interactor.send(serializer.serialize(request));
        establishInitConnection();
        while(this.active) {
            String message = inputController.getInput();
            if(message != null) request.changeMessage(message.trim());
            try {
                if(message.equals("@connect") || message.equals("exit")) request.setRoutes(0);
                if(!this.connected && message.equals("exit")) {
                    System.out.println("Shutting down the program...");
                    this.active = false;
                    continue;
                }
                else if(inputController.checkCommand() == null && this.connected) { // Checks is it command or not
                    System.out.println(inputController.getInputErrorReason());
                    continue;
                } else if(!this.connected && !message.equals("@connect"))request.setRoutes(this.dataManager.getRoutesCreated());
                // All non-commands will not pass here so we need to find a local reply or send packet to the server
                executeCommand(message);
            } catch(NullFieldException e) {
                this.connected = false;
                System.out.printf("[ERROR]: %s. Try to type @connect\n", e.getMessage());
                continue;
            }
        }
        inputController.disable();
        this.interactor.close();
    }

    public void setConnectedState() {
        this.connected = true;
    }

    public void setDisconnectedState() {
        this.connected = false;
    }
}
