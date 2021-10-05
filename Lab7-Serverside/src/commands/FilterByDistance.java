package commands;
import core.ConsolerMode;
import core.Globals;
import exceptions.NonNumberStringException;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class FilterByDistance extends AbCommand {
    public final VectorCore vector;
    private static String[] args = {"int"};

    /**
     * @param vector
     */
    public FilterByDistance(VectorCore vector) {
        super("filter_by_distance", "Displays all elements of the collection with distance value given by user", "filter_by_distance", false, false, args);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(args.length <= 0) return "There is no number as an argument in the message";
        else if(!Globals.isDouble(args[0])) return "Distance should be a number";
        else {
            try {
                double distance = Globals.getDouble(args[0]);
                String message = "\n";
                long foundElements = 0;
                for (Route route : this.vector.getVector()) {
                    if(route.getDistance() != null && route.getDistance() == distance) {
                        message += this.vector.getElementInfo(route);
                    }
                }
                if(foundElements == 0) message = "";
                return "Found " + foundElements + " elements with distance equals to " + distance + message;
            } catch(NonNumberStringException e) {
                return "Distance should be a number";
            } catch(Exception e) {
                return e.getMessage();
            }
        }
    }
}
