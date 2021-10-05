package utils.database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Vector;
import misc.*;
import utils.User;

public class DatabaseCollectionManager {

    //sql pre-statements
    private final String SELECT_ALL_ROUTES = "SELECT * FROM " + DatabaseManager.ROUTES_TABLE;
    private final String SELECT_COORDINATES_BY_ROUTE_ID = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE + " WHERE " + DatabaseManager.COORDINATES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String SELECT_LOCATIONS_FROM_BY_ROUTE_ID = "SELECT * FROM " + DatabaseManager.LOCATIONS_FROM_TABLE + " WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String INSERT_ROUTE = "INSERT INTO " + DatabaseManager.ROUTES_TABLE + "(" + DatabaseManager.ROUTES_TABLE_ROUTE_ID_COLUMN + ", " + DatabaseManager.ROUTES_TABLE_CREATOR_ID_COLUMN + ", " + DatabaseManager.ROUTES_TABLE_NAME_COLUMN + ", " + DatabaseManager.ROUTES_TABLE_CREATION_DATE_COLUMN + ", " + DatabaseManager.ROUTES_TABLE_DISTANCE_COLUMN + ") VALUES(?, ?, ?, ?, ?)";
    private final String INSERT_LOCATION_FROM = "INSERT INTO " + DatabaseManager.LOCATIONS_FROM_TABLE + " (" + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + ", " + DatabaseManager.LOCATIONS_FROM_TABLE_X_COLUMN + ", " + DatabaseManager.LOCATIONS_FROM_TABLE_Y_COLUMN + ", " + DatabaseManager.LOCATIONS_FROM_TABLE_Z_COLUMN +  ", " + DatabaseManager.LOCATIONS_FROM_TABLE_NAME_COLUMN + ") VALUES (?, ?, ?, ?, ?)";
    private final String INSERT_LOCATION_TO = "INSERT INTO " + DatabaseManager.LOCATIONS_TO_TABLE + " (" + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + ", " + DatabaseManager.LOCATIONS_TO_TABLE_X_COLUMN + ", " + DatabaseManager.LOCATIONS_TO_TABLE_Y_COLUMN + ", " + DatabaseManager.LOCATIONS_TO_TABLE_Z_COLUMN +  ", " + DatabaseManager.LOCATIONS_TO_TABLE_NAME_COLUMN + ") VALUES (?, ?, ?, ?, ?)";
    private final String INSERT_COORDINATES = "INSERT INTO " + DatabaseManager.COORDINATES_TABLE + " (" + DatabaseManager.COORDINATES_TABLE_ROUTE_ID_COLUMN + ", " + DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " + DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES(?, ?, ?)";
    private final String UPDATE_ROUTE_NAME_BY_ID = "UPDATE " + DatabaseManager.ROUTES_TABLE + " SET " + DatabaseManager.ROUTES_TABLE_NAME_COLUMN + " = ? WHERE " + DatabaseManager.ROUTES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATES_X_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " + DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ? WHERE " + DatabaseManager.COORDINATES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATES_Y_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " + DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ? WHERE " + DatabaseManager.COORDINATES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_ROUTE_DISTANCE_BY_ID = "UPDATE " + DatabaseManager.ROUTES_TABLE + " SET " + DatabaseManager.ROUTES_TABLE_DISTANCE_COLUMN+ " = ? WHERE " + DatabaseManager.ROUTES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_FROM_NAME_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_FROM_TABLE + " SET " + DatabaseManager.LOCATIONS_FROM_TABLE_NAME_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_FROM_X_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_FROM_TABLE + " SET " + DatabaseManager.LOCATIONS_FROM_TABLE_X_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_FROM_Y_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_FROM_TABLE + " SET " + DatabaseManager.LOCATIONS_FROM_TABLE_Y_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_FROM_Z_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_FROM_TABLE + " SET " + DatabaseManager.LOCATIONS_FROM_TABLE_Z_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_TO_NAME_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_TO_TABLE + " SET " + DatabaseManager.LOCATIONS_TO_TABLE_NAME_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_TO_X_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_TO_TABLE + " SET " + DatabaseManager.LOCATIONS_TO_TABLE_X_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_TO_Y_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_TO_TABLE + " SET " + DatabaseManager.LOCATIONS_TO_TABLE_Y_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String UPDATE_LOCATION_TO_Z_BY_ID = "UPDATE " + DatabaseManager.LOCATIONS_TO_TABLE + " SET " + DatabaseManager.LOCATIONS_TO_TABLE_Z_COLUMN + " = ? WHERE " + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String DELETE_ROUTE_BY_ID = "DELETE FROM " + DatabaseManager.ROUTES_TABLE + " WHERE " + DatabaseManager.ROUTES_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String DELETE_LOCATION_FROM_BY_ID = "DELETE FROM " + DatabaseManager.LOCATIONS_FROM_TABLE + " WHERE " + DatabaseManager.LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String DELETE_LOCATION_TO_BY_ID = "DELETE FROM " + DatabaseManager.LOCATIONS_TO_TABLE + " WHERE " + DatabaseManager.LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE + " WHERE " + DatabaseManager.COORDINATES_TABLE_ROUTE_ID_COLUMN + " = ?";

    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager){
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
    }

    public Vector<Route> getCollection() throws SQLException {
        Vector<Route> collection = new Vector<Route>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_ALL_ROUTES, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                collection.add(createRoute(resultSet));
            }
            return collection;
        }
        catch (SQLException e){
            throw new SQLException("Ошибка во время загрузки коллекции");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    private Route createRoute(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DatabaseManager.ROUTES_TABLE_ROUTE_ID_COLUMN);
        String name = resultSet.getString(DatabaseManager.ROUTES_TABLE_NAME_COLUMN);
        ZonedDateTime creationDate = resultSet.getTimestamp(DatabaseManager.ROUTES_TABLE_CREATION_DATE_COLUMN).toInstant().atZone(ZoneId.systemDefault());
        Double distance = resultSet.getDouble(DatabaseManager.ROUTES_TABLE_DISTANCE_COLUMN);
        Location from = getLocationFromByRouteId(id);
        Location to = getLocationToByRouteId(id);
        Coordinates coordinates = getCordsByRouteId(id);
        User creator = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.ROUTES_TABLE_CREATOR_ID_COLUMN));
        return new Route((long) id, name, coordinates, creationDate, from, to, distance, creator);
    }

    private Coordinates getCordsByRouteId(Integer routeId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_COORDINATES_BY_ROUTE_ID, false);
            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                coordinates = new Coordinates(resultSet.getLong(DatabaseManager.COORDINATES_TABLE_X_COLUMN), resultSet.getInt(DatabaseManager.COORDINATES_TABLE_Y_COLUMN));
                return coordinates;
            }
            else throw new SQLException();
        }
        catch (SQLException e){
            throw new SQLException("Ошибка при выполнении запроса SELECT_COORDINATES_BY_ROUTE_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    private Location getLocationFromByRouteId(Integer routeId) throws SQLException {
        Location location;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseManager.getPreparedStatement(SELECT_LOCATIONS_FROM_BY_ROUTE_ID, false);
            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                location = new Location(resultSet.getString(DatabaseManager.LOCATIONS_FROM_TABLE_NAME_COLUMN), resultSet.getDouble(DatabaseManager.LOCATIONS_FROM_TABLE_X_COLUMN), resultSet.getInt(DatabaseManager.LOCATIONS_FROM_TABLE_Y_COLUMN), resultSet.getInt(DatabaseManager.LOCATIONS_FROM_TABLE_Z_COLUMN));
                return location;
            }
            else throw new SQLException();
        }
        catch (SQLException e){
            throw new SQLException("Ошибка при выполнении запроса SELECT_LOCATIONS_FROM_BY_ROUTE_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }
    
    private Location getLocationToByRouteId(Integer routeId) throws SQLException {
        Location location;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseManager.getPreparedStatement(SELECT_LOCATIONS_FROM_BY_ROUTE_ID, false);
            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                location = new Location(resultSet.getString(DatabaseManager.LOCATIONS_TO_TABLE_NAME_COLUMN), resultSet.getDouble(DatabaseManager.LOCATIONS_TO_TABLE_X_COLUMN), resultSet.getInt(DatabaseManager.LOCATIONS_TO_TABLE_Y_COLUMN), resultSet.getInt(DatabaseManager.LOCATIONS_TO_TABLE_Z_COLUMN));
                return location;
            }
            else throw new SQLException();
        }
        catch (SQLException e){
            throw new SQLException("Ошибка при выполнении запроса SELECT_LOCATIONS_FROM_BY_ROUTE_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public Route insertRoute(Route route, User user) throws SQLException {
        PreparedStatement preparedStatementInsertRoute = null;
        PreparedStatement preparedStatementInsertCoords = null;
        PreparedStatement preparedStatementInsertLocFrom = null;
        PreparedStatement preparedStatementInsertLocTo = null;
        try {
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();
            preparedStatementInsertRoute = databaseManager.getPreparedStatement(INSERT_ROUTE, true);
            preparedStatementInsertRoute.setInt(1, databaseUserManager.getUserIdByUsernameAndPassword(user));
            preparedStatementInsertRoute.setString(2, route.getName());
            preparedStatementInsertRoute.setTimestamp(3, Timestamp.valueOf(route.getCreationDate().toLocalDateTime()));
            preparedStatementInsertRoute.setDouble(4, route.getDistance());
            if(preparedStatementInsertRoute.executeUpdate() == 0) throw new SQLException();
            Integer routeId;
            ResultSet generatedRouteKeys = preparedStatementInsertRoute.getGeneratedKeys();
            if(generatedRouteKeys.next()){
                routeId = generatedRouteKeys.getInt(1);
            }
            else throw new SQLException();
            preparedStatementInsertCoords = databaseManager.getPreparedStatement(INSERT_COORDINATES, false);
            preparedStatementInsertCoords.setInt(1, routeId);
            preparedStatementInsertCoords.setLong(2, route.getCoordinates().getX());
            preparedStatementInsertCoords.setLong(3, route.getCoordinates().getY());
            if(preparedStatementInsertCoords.executeUpdate() == 0) throw new SQLException();

            preparedStatementInsertLocFrom = databaseManager.getPreparedStatement(INSERT_LOCATION_FROM, false);
            preparedStatementInsertLocFrom.setInt(1, routeId);
            preparedStatementInsertLocFrom.setDouble(2, route.getStartLocation().getX());
            preparedStatementInsertLocFrom.setInt(3, route.getStartLocation().getY());
            preparedStatementInsertLocFrom.setInt(4, route.getStartLocation().getZ());
            preparedStatementInsertLocFrom.setString(5, route.getStartLocation().getName());
            if(preparedStatementInsertLocFrom.executeUpdate() == 0) throw new SQLException();

            preparedStatementInsertLocTo = databaseManager.getPreparedStatement(INSERT_LOCATION_TO, false);
            preparedStatementInsertLocTo.setInt(1, routeId);
            preparedStatementInsertLocTo.setDouble(2, route.getEndLocation().getX());
            preparedStatementInsertLocTo.setInt(3, route.getEndLocation().getY());
            preparedStatementInsertLocTo.setInt(4, route.getEndLocation().getZ());
            preparedStatementInsertLocTo.setString(5, route.getEndLocation().getName());
            if(preparedStatementInsertLocTo.executeUpdate() == 0) throw new SQLException();

            databaseManager.commit();

            return new Route(routeId, route.getName(), route.getCoordinates(), route.getCreationDate(), route.getStartLocation(), route.getEndLocation(), route.getDistance(), user);
        }
        catch (SQLException e){
            databaseManager.rollback();
            throw new SQLException("Ошибка при добавления космического корабля в БД");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatementInsertRoute);
            databaseManager.closePreparedStatement(preparedStatementInsertCoords);
            databaseManager.closePreparedStatement(preparedStatementInsertLocFrom);
            databaseManager.closePreparedStatement(preparedStatementInsertLocTo);
            databaseManager.toAutoCommit();
        }

    }

    public void updateRouteById(Integer routeId, Route route) throws SQLException {
        PreparedStatement preparedStatementUpdateNameById = null;
        PreparedStatement preparedStatementUpdateCoordinatesXById = null;
        PreparedStatement preparedStatementUpdateCoordinatesYById = null;
        PreparedStatement preparedStatementUpdateDistanceById = null;
        PreparedStatement preparedStatementUpdateLocationFromNameById = null;
        PreparedStatement preparedStatementUpdateLocationFromXById = null;
        PreparedStatement preparedStatementUpdateLocationFromYById = null;
        PreparedStatement preparedStatementUpdateLocationFromZById = null;
        PreparedStatement preparedStatementUpdateLocationToNameById = null;
        PreparedStatement preparedStatementUpdateLocationToXById = null;
        PreparedStatement preparedStatementUpdateLocationToYById = null;
        PreparedStatement preparedStatementUpdateLocationToZById = null;
        try{
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();

            preparedStatementUpdateNameById = databaseManager.getPreparedStatement(UPDATE_ROUTE_NAME_BY_ID, false);
            preparedStatementUpdateCoordinatesXById = databaseManager.getPreparedStatement(UPDATE_COORDINATES_X_BY_ID, false);
            preparedStatementUpdateCoordinatesYById = databaseManager.getPreparedStatement(UPDATE_COORDINATES_Y_BY_ID, false);
            preparedStatementUpdateDistanceById = databaseManager.getPreparedStatement(UPDATE_ROUTE_DISTANCE_BY_ID, false);
            preparedStatementUpdateLocationFromNameById = databaseManager.getPreparedStatement(UPDATE_LOCATION_FROM_NAME_BY_ID, false);
            preparedStatementUpdateLocationFromXById = databaseManager.getPreparedStatement(UPDATE_LOCATION_FROM_X_BY_ID, false);
            preparedStatementUpdateLocationFromYById = databaseManager.getPreparedStatement(UPDATE_LOCATION_FROM_Y_BY_ID, false);
            preparedStatementUpdateLocationFromZById = databaseManager.getPreparedStatement(UPDATE_LOCATION_FROM_Z_BY_ID, false);
            preparedStatementUpdateLocationToNameById = databaseManager.getPreparedStatement(UPDATE_LOCATION_TO_NAME_BY_ID, false);
            preparedStatementUpdateLocationToXById = databaseManager.getPreparedStatement(UPDATE_LOCATION_TO_X_BY_ID, false);
            preparedStatementUpdateLocationToYById = databaseManager.getPreparedStatement(UPDATE_LOCATION_TO_Y_BY_ID, false);
            preparedStatementUpdateLocationToZById = databaseManager.getPreparedStatement(UPDATE_LOCATION_TO_Z_BY_ID, false);

            if(route.getName() != null) {
                preparedStatementUpdateNameById.setString(1, route.getName());
                preparedStatementUpdateNameById.setInt(2, routeId);
                if(preparedStatementUpdateNameById.executeUpdate() == 0) throw new SQLException();
            }
            if(route.getCoordinates() != null) {
                preparedStatementUpdateCoordinatesXById.setLong(1, route.getCoordinates().getX());
                preparedStatementUpdateCoordinatesXById.setInt(2, routeId);
                if (preparedStatementUpdateCoordinatesXById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateCoordinatesYById.setDouble(1, route.getCoordinates().getY());
                preparedStatementUpdateCoordinatesYById.setInt(2, routeId);
                if (preparedStatementUpdateCoordinatesYById.executeUpdate() == 0) throw new SQLException();
            }
            if(route.getStartLocation() != null) {
                preparedStatementUpdateLocationFromNameById.setString(1, route.getStartLocation().getName());
                preparedStatementUpdateLocationFromNameById.setInt(2, routeId);
                if(preparedStatementUpdateLocationFromNameById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationFromXById.setDouble(1, route.getStartLocation().getX());
                preparedStatementUpdateLocationFromXById.setInt(2, routeId);
                if(preparedStatementUpdateLocationFromXById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationFromYById.setDouble(1, route.getStartLocation().getY());
                preparedStatementUpdateLocationFromYById.setInt(2, routeId);
                if(preparedStatementUpdateLocationFromYById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationFromZById.setDouble(1, route.getStartLocation().getZ());
                preparedStatementUpdateLocationFromZById.setInt(2, routeId);
                if(preparedStatementUpdateLocationFromZById.executeUpdate() == 0) throw new SQLException();
            }
            if(route.getEndLocation() != null) {
                preparedStatementUpdateLocationToNameById.setString(1, route.getEndLocation().getName());
                preparedStatementUpdateLocationToNameById.setInt(2, routeId);
                if(preparedStatementUpdateLocationToNameById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationToXById.setDouble(1, route.getEndLocation().getX());
                preparedStatementUpdateLocationToXById.setInt(2, routeId);
                if(preparedStatementUpdateLocationToXById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationToYById.setDouble(1, route.getEndLocation().getY());
                preparedStatementUpdateLocationToYById.setInt(2, routeId);
                if(preparedStatementUpdateLocationToYById.executeUpdate() == 0) throw new SQLException();
                preparedStatementUpdateLocationToZById.setDouble(1, route.getEndLocation().getZ());
                preparedStatementUpdateLocationToZById.setInt(2, routeId);
                if(preparedStatementUpdateLocationToZById.executeUpdate() == 0) throw new SQLException();
            }
            if(route.getDistance() != null) {
                preparedStatementUpdateDistanceById.setDouble(1, route.getDistance());
                preparedStatementUpdateDistanceById.setInt(2, routeId);
                if(preparedStatementUpdateDistanceById.executeUpdate() == 0) throw new SQLException();
            }

            databaseManager.commit();
        }
        catch (SQLException e){
            databaseManager.rollback();
            throw new SQLException("Ошибка при добавления космического корабля в БД");
        }
        finally{
            databaseManager.closePreparedStatement(preparedStatementUpdateNameById);
            databaseManager.closePreparedStatement(preparedStatementUpdateCoordinatesXById);
            databaseManager.closePreparedStatement(preparedStatementUpdateCoordinatesYById);
            databaseManager.closePreparedStatement(preparedStatementUpdateDistanceById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationFromNameById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationFromXById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationFromYById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationFromZById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationToNameById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationToXById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationToYById);
            databaseManager.closePreparedStatement(preparedStatementUpdateLocationToZById);
            databaseManager.toAutoCommit();
        }
    }

    public void deleteRouteById(Integer routeId) throws SQLException {
        PreparedStatement preparedStatementDeleteRouteById = null;
        PreparedStatement preparedStatementDeleteLocationFromById = null;
        PreparedStatement preparedStatementDeleteLocationToById = null;
        PreparedStatement preparedStatementDeleteCoordinatesById = null;
        try{
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();

            preparedStatementDeleteRouteById = databaseManager.getPreparedStatement(DELETE_ROUTE_BY_ID, false);
            preparedStatementDeleteLocationFromById = databaseManager.getPreparedStatement(DELETE_LOCATION_FROM_BY_ID, false);
            preparedStatementDeleteLocationToById = databaseManager.getPreparedStatement(DELETE_LOCATION_TO_BY_ID, false);
            preparedStatementDeleteCoordinatesById = databaseManager.getPreparedStatement(DELETE_COORDINATES_BY_ID, false);

            preparedStatementDeleteLocationFromById.setInt(1, routeId);
            if(preparedStatementDeleteLocationFromById.executeUpdate() == 0) throw new SQLException();

            preparedStatementDeleteCoordinatesById.setInt(1, routeId);
            if(preparedStatementDeleteCoordinatesById.executeUpdate() == 0) throw new SQLException();

            preparedStatementDeleteRouteById.setInt(1, routeId);
            if(preparedStatementDeleteRouteById.executeUpdate() == 0) throw new SQLException();

            databaseManager.commit();

        }
        catch (SQLException e){
            databaseManager.rollback();
            throw new SQLException("Ошибка при удалении человека из БД");
        }
        finally{
            databaseManager.closePreparedStatement(preparedStatementDeleteRouteById);
            databaseManager.closePreparedStatement(preparedStatementDeleteLocationFromById);
            databaseManager.closePreparedStatement(preparedStatementDeleteLocationToById);
            databaseManager.closePreparedStatement(preparedStatementDeleteCoordinatesById);
            databaseManager.toAutoCommit();
        }
    }

    public void clearCollection() throws SQLException {
        Vector<Route> routes = getCollection();
        for(Route route: routes){
            deleteRouteById((int) route.getID());
        }
    }

}
