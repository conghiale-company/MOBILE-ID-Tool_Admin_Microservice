package DAO;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/07
 * Time: 4:36 PM
 */

import database.MySQLZNSConnUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class UserDAO {
    private static Connection conn;

    private volatile static UserDAO userDAO;
    private UserDAO() {}
    public static UserDAO getInstance() throws Exception {
        if (userDAO == null) {
            synchronized (UserDAO.class) {
                if (userDAO == null) {
                    userDAO = new UserDAO();
                }
                if (conn == null || conn.isClosed()) {
                    synchronized (ConnectorDAO.class) {
                        if (conn == null || conn.isClosed()) {
                            conn = MySQLZNSConnUtils.getMySQLConnection();
                        }
                    }
                }
            }
        }

        return userDAO;
    }
    public String addUser(String userName, String email, String personalCode, String phone, String password, String hmac, String createdBy) throws SQLException {
        String responseCode = null;

        String procedureCall = "{CALL USP_USER_INSERT(?, ?, ?, ?, ?, 1, ?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(procedureCall)) {
            stmt.setString(1, userName);
            stmt.setString(2, email);
            stmt.setString(3, personalCode);
            stmt.setString(4, phone);
            stmt.setString(5, password);
            stmt.setString(6, hmac);
            stmt.setString(7, createdBy);
            stmt.registerOutParameter(8, Types.VARCHAR);

            stmt.execute();
            responseCode = stmt.getString(8);
        }

        return responseCode;
    }

    public Map<String, String> getAllUserList() throws SQLException {
        String query = "SELECT USER_NAME, EMAIL FROM USER WHERE ENABLED";
        Map<String, String> userMap = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("USER_NAME");
                String userEmail = rs.getString("EMAIL");
                userMap.put(userName, userEmail);
            }
        }
        return userMap;
    }

    public List<String> getUsersList() throws SQLException {
        String query = "SELECT USER_NAME FROM USER WHERE ENABLED";
        List<String> usersList = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("USER_NAME");
                usersList.add(userName);
            }
        }

        return usersList;
    }
}
