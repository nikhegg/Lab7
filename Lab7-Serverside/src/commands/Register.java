package commands;
import utils.Host;
import core.ConsolerMode;
import utils.User;

public class Register extends AbCommand {
    private static String[] args = {"String", "String"};
    private Host host;

    public Register(Host host) {
        super("register", "Creates a new account", "register <login> <password>", false, true, args);
        this.host = host;
    }
    
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(args.length < 2) return "You should specify both login and password splitted by space";
        String login = args[0];
        String password = args[1];
        if(login.length() < 2) return "Login should have at least 2 characters";
        if(password.length() < 4) return "Password should have at least 4 characters";
        boolean done = this.host.getAccountLord().createNewAccount(login, password);
        if(!done) return "Cannot create account with this login";
        else return "Account was successfully created";
    }

}
