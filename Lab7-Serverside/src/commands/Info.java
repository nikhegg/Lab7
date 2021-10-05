package commands;
import core.ConsolerMode;
import misc.VectorCore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import utils.User;

public class Info extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public Info(VectorCore vector) {
        super("info", "Shows the information about collection (created at, size, etc.)", "info", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    public String execute(String[] args, ConsolerMode mode, User user) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "-----Collection info-----\nData Type: " + this.vector.getClass().getName() + "\nHash Code: " + this.vector.hashCode() + "\nInitialisation Date: " + df.format(this.vector.getCreationDate()) +
                "\nCollection Size: " + this.vector.getSize() + "\n-------------------------";
    }
}
