package commands;
import core.ConsolerMode;
import exceptions.NotOverriddenException;
import misc.Route;
import utils.User;
import java.io.Serializable;
import java.util.HashMap;

public abstract class AbCommand implements Command, Serializable {
    private final String name;
    private final String description;
    private final String usage;
    private final boolean hidden;
    private final boolean serverOnly;
    private final String[] args;

    /**
     * @param name
     * @param description
     * @param usage
     * @param hidden
     */
    public AbCommand(String name, String description, String usage, boolean hidden, boolean serverOnly, String[] args) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.hidden = hidden;
        this.serverOnly = serverOnly;
        this.args = args;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return
     */
    public String getUsage() {
        return this.usage;
    }

    /**
     * 
     * @return
     */
    public boolean isServerOnly() {
        return this.serverOnly;
    }

    /**
     * @return
     */
    public boolean isHidden() {
        return hidden;
    }

    public String[] getArgsRequirements() {
        return this.args;
    } 

    /**
     * @param args
     * @param mode
     * @throws NotOverriddenException
     */
    public String execute(String[] args, ConsolerMode mode, User user) throws NotOverriddenException {
        throw new NotOverriddenException("Execute method of \"" + this.name + "\" command is not overridden");
    }

    /**
     * @param map
     * @param args
     * @param mode
     * @throws NotOverriddenException
     */
    public String execute(HashMap<String, AbCommand> map, String[] args, ConsolerMode mode) throws NotOverriddenException {
        throw new NotOverriddenException("Execute method of \"" + this.name + "\" commands is not overridden");
    }

    public String execute(String[] args, ConsolerMode mode, Route route) throws NotOverriddenException {
        throw new NotOverriddenException("Execute method of \"" + this.name + "\" commands is not overridden");
    }

}
