package core;
import commands.*;
import misc.VectorCore;
import utils.Host;
import utils.database.DatabaseCollectionManager;
import utils.database.DatabaseManager;
import utils.database.DatabaseUserManager;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        DatabaseManager dm = new DatabaseManager("jdbc:postgresql://pg:5432/studs", "s307945", "ktc082");
        DatabaseUserManager dum = new DatabaseUserManager(dm);
        DatabaseCollectionManager dcm = new DatabaseCollectionManager(dm, dum);    
        VectorCore collection = new VectorCore(dcm);
        Host host = new Host(2228, 6128, collection);
        Commander commander = new Commander(
                new AddElement(collection),
                new AddIfMin(collection),
                new Clear(collection),
                new CountLessThanDistance(collection),
                new FilterByDistance(collection),
                new Help(),
                new Info(collection),
                new MinByDistance(collection),
                new Register(host),
                new RemoveByID(collection),
                new RemoveLast(collection),
                new Save(collection),
                new Show(collection),
                new Sort(collection),
                new Update(collection));
        Consoler consoler = new Consoler(commander, host);
        host.start();
        consoler.start();
    }
}