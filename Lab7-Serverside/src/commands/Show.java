package commands;
import java.util.stream.Stream;

import core.ConsolerMode;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class Show extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public Show(VectorCore vector) {
        super("show", "Shows current collection as a string", "show", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        StringBuilder message = new StringBuilder();
        //String message = "";
        if(mode == ConsolerMode.CLIENT) {
            Stream<Route> vector = this.vector.getSortedVector();
            vector.forEach(route -> {
                message.append(this.vector.getElementInfo(route));
            });
            return message.toString();
        } else {
            for(int i = 0; i<this.vector.getVector().size(); i++) {
                Route route = vector.getVector().get(i);
                message.append(this.vector.getElementInfo(route));
            }
        }
        if(message.toString().equals("")) return "The collection has 0 elements inside";
        else return "The collection has " + this.vector.getSize() + " elements inside\n" + message;
    }
}
