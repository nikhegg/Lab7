package commands;
import core.ConsolerMode;
import core.Globals;
import exceptions.NonNumberStringException;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class CountLessThanDistance extends AbCommand {
    private final VectorCore vector;
    private static String[] args = {"int"};

    /**
     * @param vector
     */
    public CountLessThanDistance(VectorCore vector) {
        super("count_less_than_distance", "Counts how many elements with distance that is lower than an entered value", "count_less_than_distance <distance>",false, false, args);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(args.length <= 0) return "There is no value in the message";
        else if(!Globals.isDouble(args[0])) return "Minimal value should be a number";
        else {
            try {
                double enteredValue = Globals.getDouble(args[0]);
                if(enteredValue < 0) return "Distance cannot be negative";
                else {
                    int count = 0;
                    for (Route route : this.vector.getVector()) {
                        if(route.getDistance() != null && route.getDistance() < enteredValue) count += 1;
                    }
                    return "There are " + count + " elements in a collection with the distance less than " + enteredValue;
                }
            } catch(NonNumberStringException e) {
                return "Minimal value should be a number";
            } catch(Exception e) {
                return e.getMessage();
            }
        }
    }
}
