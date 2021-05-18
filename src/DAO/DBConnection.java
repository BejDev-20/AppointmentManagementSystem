package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsible for the connection to the database and helps maintaining connection when needed
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class DBConnection extends Thread {
    // JDBC URL parts
    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR_NAME = ":mysql:";
    private static final String IP_ADDRESS = "//wgudb.ucertify.com/WJ07OFg";

    private static final String JDBC_URL = PROTOCOL + VENDOR_NAME + IP_ADDRESS + "?autoReconnect=true";
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;
    // username and password for connection to the database
    private static final String USERNAME = "U07OFg";
    private static final String PASSWORD = "53689083159";

    /**
     * Starts the connection if it is null or returns the existing one
     * @return the newly created connection or the existing one
     */
    public static Connection startConnection(){
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Close the existing connection
     */
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the connection, creates one if one doesn't exist
     * @return connection - newly created one or one that exists
     * @throws SQLException when the connection cannot be established
     */
    public static Connection getConn(){
        try {
            if (conn == null || conn.isClosed()){
                startConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
