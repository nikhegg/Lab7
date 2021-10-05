package utils.database;

import java.sql.SQLException;
import java.util.HashMap;

import utils.Host;
import utils.User;

public class AccountLord {
    private HashMap<String, String> accounts;
    private HashMap<String, Boolean> activeSessions;
    private Host host;

    public AccountLord(Host host) {
        this.host = host;
        this.activeSessions = new HashMap<String, Boolean>();
    }

    public void logInUser(String login, String password) throws Exception {
        if(!isValidAccount(login, password)) throw new Exception("Invalid login or password");
        if(this.activeSessions.containsKey(login)) throw new Exception("Account has an active session");
        this.activeSessions.put(login, true);
    }

    public void logOutUser(String login, String password) throws Exception {
        if(!this.host.getDUM().checkUserByUsernameAndPassword(new User(login, password))) throw new Exception("Invalid login or password");
        if(this.activeSessions.containsKey(login)) throw new Exception("Account has an active session");
        this.activeSessions.remove(login);
    }

    public boolean createNewAccount(String login, String password) {
        if(this.accounts.containsKey(login)) return false;
        if(login.length() < 2) return false;
        if(password.length() < 4) return false;
        try {
            this.host.getDUM().insertUserToDatabase(new User(login, password));
        } catch(SQLException e) {
            return false;
        } catch(Exception e) {
            return false;
        }
        return true;   
    }

    public boolean isValidAccount(String login, String password) {
        try {
            return this.host.getDUM().checkUserByUsernameAndPassword(new User(login, password));
        } catch(SQLException e) {
            return false;
        } catch(Exception e) {
            return false;
        }
        
    }
    
    public boolean isOnline(String login) {
        return this.activeSessions.containsKey(login);
    }
}
