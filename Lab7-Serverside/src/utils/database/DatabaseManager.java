package utils.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    // Table names
    public static final String USERS_TABLE = "users";
    public static final String ROUTES_TABLE = "routes";
    public static final String LOCATIONS_FROM_TABLE = "locationsfrom";
    public static final String LOCATIONS_TO_TABLE = "locationsto";
    public static final String COORDINATES_TABLE = "coordinates";

    /// USERS_TALBE Columns
    public static final String USERS_TABLE_ID_COLUMN = "id";
    public static final String USERS_TABLE_USERNAME_COLUMN = "username";
    public static final String USERS_TABLE_PASSWORD_COLUMN = "password;";

    // ROUTES_TABLE Columns
    public static final String ROUTES_TABLE_ROUTE_ID_COLUMN = "route_id";
    public static final String ROUTES_TABLE_CREATOR_ID_COLUMN = "creator_id";
    public static final String ROUTES_TABLE_NAME_COLUMN = "name";
    public static final String ROUTES_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String ROUTES_TABLE_DISTANCE_COLUMN = "distance";

    // LOCATIONS_FROM_TABLE Columns
    public static final String LOCATIONS_FROM_TABLE_LOCATION_ID_COLUMN = "location_id";
    public static final String LOCATIONS_FROM_TABLE_ROUTE_ID_COLUMN = "route_id";
    public static final String LOCATIONS_FROM_TABLE_X_COLUMN = "x";
    public static final String LOCATIONS_FROM_TABLE_Y_COLUMN = "y";
    public static final String LOCATIONS_FROM_TABLE_Z_COLUMN = "z";
    public static final String LOCATIONS_FROM_TABLE_NAME_COLUMN = "name";
    
    // LOCATIONS_TO_TABLE Columns
    public static final String LOCATIONS_TO_TABLE_LOCATION_ID_COLUMN = "location_id";
    public static final String LOCATIONS_TO_TABLE_ROUTE_ID_COLUMN = "route_id";
    public static final String LOCATIONS_TO_TABLE_X_COLUMN = "x";
    public static final String LOCATIONS_TO_TABLE_Y_COLUMN = "y";
    public static final String LOCATIONS_TO_TABLE_Z_COLUMN = "z";
    public static final String LOCATIONS_TO_TABLE_NAME_COLUMN = "name";

    // COORDINATES_TABLE Columns
    public static final String COORDINATES_TABLE_COORDINATES_ID_COLUMN = "coordinates_id";
    public static final String COORDINATES_TABLE_ROUTE_ID_COLUMN = "route_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    
    private final String address;
    private final String user;
    private final String password;
    private final String DRIVER = "org.postgresql.Driver";
    private Connection connection;


    public DatabaseManager(String address, String user, String password){
        this.address = address;
        this.user = user;
        this.password = password;
        connectToDatabase();
    }

    private void connectToDatabase(){
        System.out.println("Попытка подключения к БД");
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(address, user, password);
            System.out.println("Подключились к БД");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер для данной БД не найден");
        }
        catch (SQLException e){
            System.out.println("Ошибка при подключении к БД");
        }

    }

    public void closeConnection(){
        if(connection == null) return;
        try {
            connection.close();
        }
        catch (SQLException e){
            System.out.println("Ошибка при закрытии БД");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generationMode) throws SQLException {
        if(connection == null) throw new SQLException("Ошибка при формировании запроса");
        int key = generationMode ? PreparedStatement.RETURN_GENERATED_KEYS : PreparedStatement.NO_GENERATED_KEYS;
        return connection.prepareStatement(sqlStatement, key);
    }

    public void closePreparedStatement(PreparedStatement preparedStatement){
        if(preparedStatement == null) return;
        try {
            preparedStatement.close();
        } catch (SQLException e) {

        }
    }

    public void toNonAutoCommit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время изменения режима сохранения");
            connection.setAutoCommit(false);
        }
        catch (SQLException e){
            //ServerMain.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void toAutoCommit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время изменения режима сохранения");
            connection.setAutoCommit(true);
        }
        catch (SQLException e){
        }
    }

    public void commit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время сохранения состояния БД");
            connection.commit();
        }
        catch (SQLException e){

        }
    }

    public void rollback(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время отката состояния БД");
            connection.rollback();
        }
        catch (SQLException e){

        }

    }

    public void addSavePoint(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время добавления точки сохранения");
            connection.setSavepoint();
        }
        catch (SQLException e){
            
        }

    }
}
