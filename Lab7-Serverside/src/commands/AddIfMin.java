package commands;
import core.ConsolerMode;
import core.Globals;
import core.RouteMaker;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class AddIfMin extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public AddIfMin(VectorCore vector) {
        super("add_if_min", "Adds an element to the collection if it has the smallest value in comparison with elements of the collection", "add_if_min", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        Route route = new RouteMaker().makeNewRoute(mode, user);

        if(route != null) {
            Double minDistance = -1.0;
            for (Route vectorRoute : this.vector.getVector()) {
                if(route.getDistance() != null && vectorRoute.getDistance() != null && (vectorRoute.getDistance() < minDistance || minDistance == -1.0)) minDistance = vectorRoute.getDistance();
            }
            if(route.getDistance() == null || minDistance > route.getDistance() || minDistance == -1.0) {
                this.vector.addElement(route);
                return "\nNew route was successfully added to the collection";
            } else {
                Globals.decRoutesCreated();
                return "\nRoute " + route.getName() + " has not the smallest value of distance so it was not added to the collection";
            }
        }
        return "Can't add a new route to the collection";
    }

    @Override
    public String execute(String[] args, ConsolerMode mode, Route route) {
        if(route != null) {
            Double minDistance = -1.0;
            for (Route vectorRoute : this.vector.getVector()) {
                if(route.getDistance() != null && vectorRoute.getDistance() != null && (vectorRoute.getDistance() < minDistance || minDistance == -1.0)) minDistance = vectorRoute.getDistance();
            }
            if(route.getDistance() == null || minDistance > route.getDistance() || minDistance == -1.0) {
                Globals.incRoutesCreated();
                this.vector.addElement(route);
                return "\nNew route was successfully added to the collection";
            } else {
                return "\nRoute " + route.getName() + " has not the smallest value of distance so it was not added to the collection";
            }
        }
        return "Can't add a new route to the collection";
    }
}
