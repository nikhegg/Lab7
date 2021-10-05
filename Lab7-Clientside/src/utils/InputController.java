package utils;
import java.util.Scanner;

public class InputController {
    private boolean enabled;
    private Scanner scanner;
    private String lastInput;
    private final CommandVerifier verifier;

    InputController(CommandVerifier verifier) {
        this.verifier = verifier;
        enable();
    }

    public void enable() {
        this.scanner = new Scanner(System.in);
        this.enabled = true;
        this.lastInput = "";
    }

    public void disable() {
        this.scanner.close();
        this.enabled = false;
        this.lastInput = "";
    }

    public String getInput() {
        if(!this.enabled) return null;
        else {
            String message = this.scanner.nextLine();
            this.lastInput = message;
            /*if(!verifier.doesExist(message) || verifier.meetsCommandRequirements(message) != null) return null;
            else return message;*/
            return message;
        }
    }

    public String checkCommand() {
        if(!this.enabled) return null;
        String command = this.lastInput.split(" ")[0];
        switch(this.lastInput) {
            case "@connect":
                return this.lastInput;
            case "exit":
                return this.lastInput;
            default:
                if(!verifier.doesExist(command) || verifier.meetsCommandRequirements(this.lastInput) != null) return null;
                else return this.lastInput;
        }
    }

    public String getInputErrorReason() {
        return verifier.meetsCommandRequirements(this.lastInput);
    }
}
