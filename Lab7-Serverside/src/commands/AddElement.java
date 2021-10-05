package commands;
import core.ConsolerMode;
import core.Globals;
import core.RouteMaker;
import misc.VectorCore;
import misc.Route;
import utils.User;

public class AddElement extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public AddElement(VectorCore vector) {
        super("add", "Adds a new element to the collection", "add", false, false, new String[0]);
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
            this.vector.addElement(route);
            return "New route successfully added to the collection";
        }
        return "Can't add a new route to the collection";
    }

    @Override
    public String execute(String[] args, ConsolerMode mode, Route route) {
        if(route != null) {
            this.vector.addElement(route);
            Globals.incRoutesCreated();
            return "New route successfully added to the collection";
        }
        return "Can't add a new route to the collection";
    }
}
