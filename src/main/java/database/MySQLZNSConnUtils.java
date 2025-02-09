package database;

import view.modal.ConfigInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLZNSConnUtils {

    private static Connection conn;

    public static Connection getMySQLConnection() throws Exception {
        String hostName = ConfigInfo.getInstance().getHostname();
        String dbName = ConfigInfo.getInstance().getDbName();
        String userName = ConfigInfo.getInstance().getUserName();
        String password = ConfigInfo.getInstance().getPassword();

        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName, String userName, String password) throws Exception {
        if (conn == null || conn.isClosed()) {
            try {
                String connectionURL = "jdbc:mariadb://" + hostName + ":3306/" + dbName;
                Class.forName("org.mariadb.jdbc.Driver");
                conn = DriverManager.getConnection(connectionURL, userName, password);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to connect to the database.");
            }
        }
        return conn;
    }

    public static void CloseMySQLConnection(Connection conn) throws SQLException {
        if (!conn.isClosed())
            conn.close();
    }
}
