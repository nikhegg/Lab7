package commands;
import core.ConsolerMode;
import core.Globals;
import core.RouteMaker;
import exceptions.NonNumberStringException;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class Update extends AbCommand {
    private final VectorCore vector;
    private static String[] args = {"int"};

    /**
     * @param vector
     */
    public Update(VectorCore vector) {
        super("update", "Updates an elements of collection with a specified ID", "update <id>", false, false, args);
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
                if((this.vector.getSize()-1) < id) return "The collection does not have an element with ID " + id;
                else {
                    int key;
                    Route route = null;
                    key = Integer.parseInt(args[0]);
                    route = new RouteMaker().makeNewRoute(mode ,user);
                    if(route != null) this.vector.updateID(key, route);
                    return "Element with ID " + id + " was successfully updated";
                }
            } catch(NonNumberStringException e) {
                return "ID should be a number";
            } catch(Exception e) {
                return e.getMessage();
            }
        }
    }

    @Override
    public String execute(String[] args, ConsolerMode mode, Route route) {
        if(mode != ConsolerMode.CLIENT) return "This way of creation a new route is only available for clients";
        if(args.length <= 0) return "There is no ID in the message";
        else if(!Globals.isInt(args[0])) return "ID should be a number";
        else try {
            int id = Globals.getInt(args[0]);
            if((this.vector.getSize()-1) < id) return "The collection does not have an element with ID " + id;
            else {
                int key = Integer.parseInt(args[0]);
                if(route != null) this.vector.updateID(key, route);
                return "Element with ID " + id + " was successfully updated";
            }
        } catch(NonNumberStringException e) {
            return "ID should be a number";
        } catch(Exception e) {
            return e.getMessage();
        }
    }
}
