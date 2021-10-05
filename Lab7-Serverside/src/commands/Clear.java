package commands;
import core.ConsolerMode;
import misc.VectorCore;
import java.util.Scanner;
import utils.User;

public class Clear extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public Clear(VectorCore vector) {
        super("clear", "Clears the collection", "clear", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(mode == ConsolerMode.CLIENT) {
            this.vector.clear();
            return "The collection was successfully cleared";
        }
        System.out.print("Are you sure that you want to clear the collection?\nType Y for \"yes\" or N for \"no\": ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        scanner.close();
        char confirmationLetter;
        if(line.split("").length > 1) confirmationLetter = 'F';
        else confirmationLetter = line.charAt(0);
        switch(confirmationLetter) {
            case 'Y':
            case 'y':
                this.vector.clear();
                return "The collection was successfully cleared";
            case 'N':
            case 'n':
                return "The clearance of the collection was aborted";
            default:
                return "Should get Y/N value, got " + line + "\nThe clearance of the collection was aborted";
        }
        
    }
}
