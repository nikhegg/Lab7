package commands;
import core.ConsolerMode;
import misc.Route;
import misc.VectorCore;
import java.util.Vector;
import utils.User;

public class MinByDistance extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public MinByDistance(VectorCore vector) {
        super("min_by_distance", "Finds one of elements in the collection which has the less distance parameter", "min_by_distance", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        if(this.vector.getSize() <= 0) return "The collection is empty";
        else {
            Double distance = null;
            for (Route route : this.vector.getVector()) if(route.getDistance() != null && (distance == null || route.getDistance() < distance)) distance = route.getDistance();
            if(distance != null) {
                Vector<Route> smallestRoutes = new Vector<>();
                for (Route route : this.vector.getVector()) {
                    if(route.getDistance() != null && route.getDistance().equals(distance)) {
                        smallestRoutes.add(route);
                    }
                }
                if(smallestRoutes.size() > 0) {
                    int random = (int) Math.floor(Math.random()*smallestRoutes.size());
                    Route chosenRoute = smallestRoutes.get(random);
                    return this.vector.getElementInfo(chosenRoute);
                } else return "All elements of collection has no distance parameter";
            } else return "All elements of the collection has no distance parameter";

        }
    }
}
