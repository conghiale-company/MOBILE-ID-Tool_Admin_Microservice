package controller;

import model.*;
import view.MainView;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.List;
import static database.MySQLZNSConnUtils.getMySQLConnection;

public class AuditLogController {
    private Connection connection;
    private MainView view;
    private List<AuditLog> auditLogList;

    public AuditLogController(MainView view) {
        this.view = view;
        this.auditLogList = new ArrayList<>();
        try {
            connection = getMySQLConnection();
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Error connecting to the database: " + e.getMessage());
        }

        loadAuditLog();
    }

    private void loadAuditLog() {
        try (CallableStatement stmt = connection.prepareCall("{CALL USP_AUDIT_LOG_LIST()}")) {
            // Execute the stored procedure
            ResultSet resultSet = stmt.executeQuery();

            // Clear existing rows from the config table
            view.getAuditLogTableModel().setRowCount(0);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

            // Iterate through the result set and populate the config table
            while (resultSet.next()) {
                AuditLog auditlog = new AuditLog();
                auditlog.setId(resultSet.getLong("ID"));
                int status = resultSet.getInt("ENABLED");
                String statusStr = (status == 1) ? "ACTIVE" : "INACTIVE";
                auditlog.setUri(resultSet.getLong("URI"));
                auditlog.setRequestIP(resultSet.getString("REQUEST_IP"));
                auditlog.setRequest(resultSet.getString("REQUEST"));
                auditlog.setResponse(resultSet.getString("RESPONSE"));
                auditlog.setHmac(resultSet.getString("HMAC"));
                auditlog.setCreateBy(resultSet.getString("CREATED_BY"));
                auditlog.setCreateAt(resultSet.getDate("CREATED_AT"));

                auditLogList.add(auditlog);
                view.getAuditLogTableModel().addRow(new Object[]{
                        auditlog.getId(),
                        statusStr,
                        auditlog.getUri(),
                        auditlog.getRequestIP(),
                        auditlog.getRequest(),
                        auditlog.getResponse(),
                        auditlog.getHmac(),
                        auditlog.getCreateBy(),
                        dateFormat.format(auditlog.getCreateAt())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showError("Error retrieving connector attributes: " + e.getMessage());
        }
    }
}
