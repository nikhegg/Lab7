package commands;
import utils.User;
import core.ConsolerMode;
import exceptions.NotOverriddenException;

public interface Command {
    /**
     * @param args
     * @param mode
     * @throws NotOverriddenException
     */
    String execute(String[] args, ConsolerMode mode, User user) throws NotOverriddenException;
}
