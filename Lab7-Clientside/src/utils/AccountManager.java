package utils;
import java.util.Scanner;

public class AccountManager {
    private User account;

    public User getAccount() {
        return this.account;
    }

    private boolean checkLoginRequirements(String login) {
        if(login.length() < 2) return false;
        else return true;
    }

    private boolean checkPasswordRequirements(String password) {
        if(password.length() < 4) return false;
        else return true;
    }

    public boolean requireNewAccount() {
        try {
            // Handle New Account Creation
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nEnter your login: ");
            String login = scanner.nextLine();
            while(!checkLoginRequirements(login)) {
                System.out.print("\nError: Login cannot has less than 2 characters\n\nEnter your login: ");
                login = scanner.nextLine();
            }
            System.out.print("\nEnter your password: ");
            String password = scanner.nextLine();
            while(!checkPasswordRequirements(password)) {
                System.out.print("\nError: Password cannot has less than 4 character\n\nEnter your password: ");
                password = scanner.nextLine();
            }
            //scanner.close();
            this.account = new User(login, password);
            return true;
        } catch (Exception e) {
            System.out.printf("Program error: %s\n", e.getMessage());
            return false;
        }

    }
}
