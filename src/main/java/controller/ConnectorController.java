package controller;

import DAO.ConnectorDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.*;
import view.MainView;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static database.MySQLZNSConnUtils.getMySQLConnection;

public class ConnectorController {
    private Connection connection;
    private MainView view;
    private Identity model;
    private List<ConnectorModel> connectorList;

    public ConnectorController(MainView view) {
        this.view = view;
        this.connectorList = new ArrayList<>();
        try {
            connection = getMySQLConnection();
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Error connecting to the database: " + e.getMessage());
        }

        loadConnectorAttributes();
    }

    private void loadConnectorAttributes() {
        try (CallableStatement stmt = connection.prepareCall("{CALL USP_CONNECTOR_LIST()}")) {
            // Execute the stored procedure
            ResultSet resultSet = stmt.executeQuery();

            // Clear existing rows from the connector table
            view.getConnectorTableModel().setRowCount(0);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

            // Iterate through the result set and populate the connector table
            while (resultSet.next()) {
                ConnectorModel connector = new ConnectorModel();
                connector.setId(resultSet.getLong("ID"));
                int enabled = resultSet.getInt("ENABLED");
                String enabledStr = (enabled == 1) ? "ACTIVE" : "INACTIVE";
                connector.setProvider(resultSet.getString("PROVIDER_NAME"));
                connector.setName(resultSet.getString("NAME"));
                connector.setRemark(resultSet.getString("REMARK"));
                connector.setRemarkEn(resultSet.getString("REMARK_EN"));
                connector.setProperties(resultSet.getString("PROPERTIES"));
                connector.setCallbackUrl(resultSet.getString("CALLBACK_URL"));
                connector.setCreatedBy(resultSet.getString("CREATED_BY"));
                connector.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));

                connectorList.add(connector);
                view.getConnectorTableModel().addRow(new Object[]{
                        connector.getId(),
                        enabledStr,
                        connector.getProvider(),
                        connector.getName(),
                        connector.getRemark(),
                        connector.getRemarkEn(),
                        connector.getProperties(),
                        connector.getCallbackUrl(),
                        connector.getCreatedBy(),
                        dateFormat.format(connector.getCreatedAt())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showError("Error retrieving connector attributes: " + e.getMessage());
        }
    }

    public void addConnector() {
        String connectorName = view.txtConnectorName.getText();
        String providerName = view.txtConnectorProvider.getText();
        String callbackURL = view.txtConnectorCallBackURL.getText();
        String remark = view.txtConnectorRemark.getText();
        String logoPath = view.lblConnectorLogoPath.getText();
        String base64Logo = encodeFileToBase64(logoPath);
        String properties = view.txtConnectorProperties.getText();
        String createdBy = view.txtConnectorCreatedBy.getText();

        try {
            String jsonInput = String.format(
                    "{\"connectorName\":\"%s\",\"provider\":\"%s\",\"callBackURL\":\"%s\",\"remark\":\"%s\"}",
                    connectorName, providerName, callbackURL, remark
            );

            String responseCode = ConnectorDAO.getInstance().addConnector(connectorName, providerName, callbackURL, remark, properties, createdBy);

            if ("0".equals(responseCode)) {
                JOptionPane.showMessageDialog(null, "Connector added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding Connector. Response Code: " + responseCode);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String encodeFileToBase64(String filePath) {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            return Base64.getEncoder().encodeToString(fileBytes);  // Convert to Base64
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fillConnectorPanel(ConnectorModel Connector, JLabel lblConnectorLogoImage) {
        view.txtConnectorName.setText(Connector.getName());
        view.txtConnectorProvider.setText(Connector.getProvider());
        view.txtConnectorCallBackURL.setText(Connector.getCallbackUrl());
        view.txtConnectorRemark.setText(Connector.getRemark());

        String connectorString = Connector.getProperties();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(connectorString, Object.class);
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String prettyJson = writer.writeValueAsString(jsonObject);

            view.txtConnectorProperties.setText(prettyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int status = Connector.getEnabled();
        if (status == 1) {
            view.rbEnabled.setSelected(true);
            view.rbDisabled.setSelected(false);
        } else {
            view.rbEnabled.setSelected(false);
            view.rbDisabled.setSelected(true);
        }

        view.txtConnectorCreatedBy.setText(Connector.getCreatedBy());
    }

    public void editController() {
        Long connectorID = view.getSelectedRowID;
        String connectorName = view.txtConnectorName.getText();
        String providerName = view.txtConnectorProvider.getText();
        String callbackURL = view.txtConnectorCallBackURL.getText();
        String remark = view.txtConnectorRemark.getText();
        String remarkEn = view.txtConnectorRemark.getText();
        String properties = view.txtConnectorProperties.getText();
        String createdBy = view.txtConnectorCreatedBy.getText();

        try {
            String responseCode = ConnectorDAO.getInstance().updateConnector(connectorID, connectorName, providerName, remark, remarkEn, properties, callbackURL, createdBy);

            if ("0".equals(responseCode)) {
                JOptionPane.showMessageDialog(null, "Connector updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error updating Connector. Response Code: " + responseCode);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteConnector(Long identityID) {
        try {
            String response = ConnectorDAO.getInstance().deleteConnector(view.getSelectedRowID);
            if ("0".equals(response)) {
                JOptionPane.showMessageDialog(null, "Delete Success.");
                refreshConnectorTable();
            } else {
                JOptionPane.showMessageDialog(null, "Delete failed. Row not found or could not be deleted.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while deleting.");
        }
    }

    public void refreshConnectorTable() throws Exception {
//        Xóa toàn bộ dữ liệu cũ trong bảng
        view.ConnectorTableModel.setRowCount(0);

//        Lấy danh sách doanh nghiệp từ cơ sở dữ liệu
        List<ConnectorModel> ConnectorList = ConnectorDAO.getInstance().reloadConnector();

//        Thêm lại dữ liệu mới từ danh sách
        for (ConnectorModel Connector : ConnectorList) {
            view.ConnectorTableModel.addRow(new Object[]{
                    Connector.getId(),
                    Connector.getEnabled() == 1 ? "ACTIVE" : "INACTIVE",
                    Connector.getProvider(),
                    Connector.getName(),
                    Connector.getRemark(),
                    Connector.getRemarkEn(),
                    Connector.getProperties(),
                    Connector.getCallbackUrl(),
                    Connector.getCreatedBy(),
                    Connector.getCreatedAt()
            });
        }
    }

}
