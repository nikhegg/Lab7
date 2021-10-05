package utils;
import java.io.Serializable;

public class ClientCommand implements Serializable {
    private final String name;
    private final String description;
    private final String usage;
    private final String[] args;

    ClientCommand(String name, String description, String usage, String[] args) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.args = args;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public String[] getArgs() {
        return this.args;
    }
}
