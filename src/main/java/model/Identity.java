package model;

import java.sql.*;
import java.util.Date;

import static database.MySQLZNSConnUtils.getMySQLConnection;

public class Identity {
    private Long id;
    private int enabled;
    private String name;
    private String remark_en;
    private String remark;
    private String clientID;
    private String clientSecret;
    private String properties;
    private String createdBy;
    private Timestamp createdAt;
    private String modifiedBy;
    private Timestamp modifiedAt;

    public Identity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return (Timestamp) createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getRemark_en() {
        return remark_en;
    }

    public void setRemark_en(String remark_en) {
        this.remark_en = remark_en;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    //    public String addIdentityConnector(String identityName,
//                                         String connectorName,
//                                         String appId,
//                                         String secretKey,
//                                         int defaultEnabled,
//                                         String createdBy
//    ) throws SQLException {
//        reloadIdentities();
//        String responseCode = null;
//        CallableStatement callableStatement = null;
//
//        try (Connection connection = connectToDatabase();
//             CallableStatement stmt = connection.prepareCall("{CALL USP_BO_IDENTITY_ADD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
//        ) {
//            // Prepare the stored procedure call
//            String storedProcedure = "{CALL USP_BO_IDENTITY_CONNECTOR_ADD(?, ?, ?, ?, ?, ?, ?)}";
//            callableStatement = connection.prepareCall(storedProcedure);
//
//            // Set input parameters
//            callableStatement.setString(1, identityName);
//            callableStatement.setString(2, connectorName);
//            callableStatement.setString(3, appId);
//            callableStatement.setString(4, secretKey);
//            callableStatement.setInt(5, defaultEnabled);
//            callableStatement.setString(6, createdBy);
//
//            // Register the output parameter
//            callableStatement.registerOutParameter(7, Types.VARCHAR);
//
//            // Execute the stored procedure
//            callableStatement.execute();
//
//            // Retrieve the output parameter
//            responseCode = callableStatement.getString(7);
//            System.out.println("Response code: " + responseCode);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            throw ex;  // Rethrow to let the caller handle it
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return responseCode;
//    }

}

