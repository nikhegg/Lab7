package utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {
    private String name;
    private String password;

    public User(String login, String pword) throws Exception {
        if(login.length() < 2) throw new Exception("Login should have at least 2 characters");
        if(pword.length() < 1) throw new Exception("Password cannot be empty");
        else if(pword.length() < 4) throw new Exception("Password should have at least 4 characters");
        this.name = login;
        try {
            MessageDigest hasher = MessageDigest.getInstance("MD5");
            StringBuilder builder = new StringBuilder();
            builder.append(new BigInteger(1, hasher.digest(pword.getBytes())).toString(16));
            if(builder.length() < 1) throw new Exception("Password is empty");
            while(builder.toString().length() < 32) {
                builder.insert(0, '0');
            }
            this.password = builder.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new Exception("Invalid hashing algorithm");
        }
    }

    public String getName() {
        return this.name;
    }
    
    public String getPassword() {
        return this.password;
    }
}