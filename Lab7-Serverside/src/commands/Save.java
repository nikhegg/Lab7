package commands;
import core.ConsolerMode;
import misc.VectorCore;
import utils.User;

public class Save extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public Save(VectorCore vector) {
        super("save", "Saves the collection to a file", "save", false, true, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        this.vector.getClass();
        String result = "";
        return result;
    }
}
