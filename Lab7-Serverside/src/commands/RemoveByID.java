package commands;
import core.ConsolerMode;
import core.Globals;
import exceptions.NonNumberStringException;
import misc.VectorCore;
import utils.User;

public class RemoveByID extends AbCommand {
    private final VectorCore vector;
    private static String[] args = {"int"};

    /**
     * @param vector
     */
    public RemoveByID(VectorCore vector) {
        super("remove_by_id", "Finds an element in the collection by ID and deletes it", "remove_by_id <id>", false, false, args);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(args.length <= 0) return "There is no ID in the message";
        else if(!Globals.isInt(args[0])) return "ID should be a number";
        else {
            try {
                int id = Globals.getInt(args[0]);
                if((this.vector.getSize()-1) < id || id < 0) return "The collection does not have an element with ID " + id;
                else {
                    int key;
                    key = Integer.parseInt(args[0]);
                    if(this.vector.getVector().get(key) != null) {
                        this.vector.getVector().remove(key);
                        return "Element with ID " + key + " was successfully removed from the collection";
                    } else return "Could not remove the element because it does not exist";
                }
            } catch(NonNumberStringException e) {
                return "ID should be a number";
            } catch(Exception e) {
                return e.getMessage();
            }
        }
    }
}
