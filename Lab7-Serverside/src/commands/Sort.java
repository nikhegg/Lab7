package commands;
import core.ConsolerMode;
import misc.Route;
import misc.VectorCore;
import utils.User;

public class Sort extends AbCommand {
    private final VectorCore vector;

    /**
     * @param vector
     */
    public Sort(VectorCore vector) {
        super("sort", "Sorts the collection in normal order", "sort", false, false, new String[0]);
        this.vector = vector;
    }

    /**
     * @param args
     * @param mode
     */
    @Override
    public String execute(String[] args, ConsolerMode mode, User user) {
        boolean sortIsDone = false;
        while(!sortIsDone) {
            sortIsDone = true;
            for(int i = 0; i<this.vector.getSize()-1; i++) {
                if(this.vector.getVector().get(i).getID() > this.vector.getVector().get(i+1).getID()) {
                    Route savedRoute = this.vector.getVector().get(i);
                    this.vector.updateID(i, this.vector.getVector().get(i+1));
                    this.vector.updateID(i+1, savedRoute);
                    sortIsDone = false;
                }
            }
        }
        String message = "";
        for(int i = 0; i < this.vector.getVector().size(); i++) message += this.vector.getElementInfo(vector.getVector().get(i));
        if(message.equals("")) return "The collection has 0 elements inside";
        else return "The collection has " + this.vector.getSize() + " elements inside (sorted by ID)\n" + message;

    }
}
