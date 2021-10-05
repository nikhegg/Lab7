package misc;
import java.io.Serializable;

import utils.User;

public class Route implements Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private Double distance; //Поле может быть null, Значение поля должно быть больше 1
    private User user;

    /**
     * @param name
     * @param coordinates
     * @param creationDate
     * @param from
     * @param to
     * @param distance
     */
    public Route(long routesCreated, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, Location from, Location to, Double distance, User user) {
        this.id = routesCreated;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.user = user;
    }

    /**
     * @return
     */
    public long getID() {
        return this.id;
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
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * @return
     */
    public java.time.ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * @return
     */
    public Location getStartLocation() {
        return this.from;
    }

    /**
     * @return
     */
    public Location getEndLocation() {
        return this.to;
    }

    /**
     * @return
     */
    public Double getDistance() {
        return this.distance;
    }

    public User getUser() {
        return this.user;
    }
}
