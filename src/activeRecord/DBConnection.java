package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    // variables a modifier en fonction de la base
    private static String userName = "root";
    private static String password = "";
    private static String serverName = "localhost";
    //Attention, sous MAMP, le port est 8889
    private static String portNumber = "3306";
    private static String tableName = "personne";

    // iL faut une base nommee testPersonne !
    private static String dbName = "testpersonne";

    private Connection connection;
    private static DBConnection instance;

    private DBConnection() throws SQLException {
        // creation de la connection
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;

        this.connection = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance.connection;
    }

    /**
     * Permet de changer la base à laquelle on se connecte
     * @param nomDB
     */
    public static void setNomDB(String nomDB) {
        dbName = nomDB;
        instance = null;
    }
}