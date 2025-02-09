package controller;

import model.*;
import org.apache.log4j.Logger;
import view.MainView;

import javax.swing.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.List;

import static database.MySQLZNSConnUtils.getMySQLConnection;

public class IdentityConnectorController {
    private static final Logger LOGGER = Logger.getLogger(IdentityConnectorController.class);

    private Connection connection;
    private MainView view;
    private List<IdentityConnector> identityConnectors;

    public IdentityConnectorController(MainView view) {
        this.view = view;
        this.identityConnectors = new ArrayList<>();
        try {
            connection = getMySQLConnection();
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            view.showError("Error connecting to the database: " + e.getMessage());
        }

        loadIdentityConnector();
    }

    private void loadIdentityConnector(){
        try (CallableStatement stmt = connection.prepareCall("{CALL USP_IDENTITY_CONNECTOR_LIST()}")) {
            // Execute the stored procedure
            ResultSet resultSet = stmt.executeQuery();

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            // Clear existing rows from the table
            view.getIConnectorTableModel().setRowCount(0);

            // Iterate through the result set and populate the table
            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                int enabled = resultSet.getInt("ENABLED");
                long identityName = resultSet.getLong("IDENTITY_NAME");
                long connectorName = resultSet.getLong("CONNECTOR_NAME");
                String description = resultSet.getString("DESCRIPTION");
                String hmac = resultSet.getString("HMAC");
                String createdBy = resultSet.getString("CREATED_BY");
                Timestamp createdAt = resultSet.getTimestamp("CREATED_AT");

                // Add rows to the identity table model
                view.getIConnectorTableModel().addRow(new Object[]{
                        id,
                        enabled == 1 ? "ACTIVE" : "INACTIVE",
                        identityName,
                        connectorName,
                        description,
                        hmac,
                        createdBy,
                        createdAt != null ? dateFormat.format(createdAt) : null
                });

            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
            view.showError("Error loading identities from the database: " + e.getMessage());
        }
    }

    public void DeleteIConnector(Long iConnectorID) {
        try {
            IdentityConnector iConnector = new IdentityConnector();
            String response = iConnector.deleteIConnector(view.getSelectedRowID);
            if ("0".equals(response)) {
                JOptionPane.showMessageDialog(null, "Delete Success.");
//                refreshIdentityTable();
            } else {
                JOptionPane.showMessageDialog(null, "Delete failed. Identity not found or could not be deleted.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while deleting.");
        }
    }


}
