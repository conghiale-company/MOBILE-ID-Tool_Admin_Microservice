package DAO;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/07
 * Time: 4:35 PM
 */

import database.MySQLZNSConnUtils;
import model.ConnectorModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class ConnectorDAO {
    private static Connection conn;

    private volatile static ConnectorDAO connectorDAO;
    private ConnectorDAO() {}
    public static ConnectorDAO getInstance() throws Exception {
        if (connectorDAO == null) {
            synchronized (ConnectorDAO.class) {
                if (connectorDAO == null) {
                    connectorDAO = new ConnectorDAO();
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

        return connectorDAO;
    }

    public ConnectorModel getConnectorByID(Long identityID) {
        ConnectorModel Connector = null;
        try {
            CallableStatement stmt = conn.prepareCall("{CALL USP_CONNECTOR_GET(?, ?)}");

            stmt.setLong(1, identityID);
            stmt.registerOutParameter(2, Types.VARCHAR);

            ResultSet rs = stmt.executeQuery();

            String responseCode = stmt.getString(2);
            if ("0".equals(responseCode)) {
                if (rs.next()) {
                    Connector = new ConnectorModel();
                    Connector.setId(rs.getLong("ID"));
                    Connector.setProvider(rs.getString("PROVIDER_NAME"));
                    Connector.setName(rs.getString("NAME"));
                    Connector.setRemark(rs.getString("REMARK"));
                    Connector.setRemarkEn(rs.getString("REMARK_EN"));
                    Connector.setProperties(rs.getString("PROPERTIES"));
                    Connector.setCallbackUrl(rs.getString("CALLBACK_URL"));
                    Connector.setCreatedBy(rs.getString("CREATED_BY"));
                    Connector.setCreatedAt(rs.getDate("CREATED_AT"));
                }
            } else {
                System.out.println("Procedure failed with response code: " + responseCode);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Connector;
    }

    public String addConnector(String name, String providerName, String remark, String remarkEn,
                               String properties, String createdBy) throws SQLException {
        CallableStatement stmt = null;
        String responseCode = "0";
        try {
            stmt = conn.prepareCall("{CALL USP_CONNECTOR_INSERT(?, ?, ?, ?, ?, ?, ?, ?)}");

            // Set input parameters
            stmt.setString(1, name);
            stmt.setString(2, providerName);
            stmt.setString(3, remark);
            stmt.setString(4, remarkEn);
            stmt.setString(5, properties);
            stmt.setString(6, createdBy);
            stmt.registerOutParameter(7, Types.VARCHAR);
            stmt.registerOutParameter(8, Types.BIGINT);

            stmt.execute();

            responseCode = stmt.getString(7); // Get the response code
            Long connectorID = stmt.getLong(8); // Get the connector ID (if needed)

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return responseCode;
    }

    public String updateConnector(Long connectorID, String name, String providerName, String callbackUrl, String remark, String remarkEn,
                                  String properties, String createdBy) throws SQLException {
        CallableStatement stmt = null;
        String responseCode = "0";

        try {
            stmt = conn.prepareCall("{CALL USP_CONNECTOR_UPDATE(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Set input parameters
            stmt.setLong(1, connectorID);               // pCONNECTOR_ID
            stmt.setString(2, name);                   // pCONNECTOR_NAME
            stmt.setString(3, providerName);               // pPROVIDER_NAME
            stmt.setString(4, remark);                 // pREMARK
            stmt.setString(5, remarkEn);                   // pREMARK_EN (optional, không được truyền)
            stmt.setString(6, properties);             // pPROPERTIES
            stmt.setString(7, callbackUrl);            // pCALLBACK_URL
            stmt.setString(8, createdBy);              // pCREATED_BY
            stmt.registerOutParameter(9, Types.VARCHAR); // pRESPONSE_CODE

            stmt.execute();

            responseCode = stmt.getString(9);          // Get the response code

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return responseCode;
    }


    public String deleteConnector(Long connectorID) {
        try {
            CallableStatement stmt = conn.prepareCall("{CALL USP_CONNECTOR_DELETE(?, ?)}");
            stmt.setLong(1, connectorID);
            stmt.registerOutParameter(2, Types.VARCHAR);

            stmt.execute();

//            Lấy mã phản hồi từ stored procedure
            return stmt.getString(2);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    public Map<String, ConnectorModel> getConnectorList() throws SQLException {
        Map<String, ConnectorModel> connectorMap = new HashMap<>();
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
//            Sử dụng CallableStatement để gọi thủ tục
            stmt = conn.prepareCall("{CALL USP_CONNECTOR_LIST()}");
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet
                String name = rs.getString("NAME");
                String provider = rs.getString("PROVIDER_NAME");
                String remark = rs.getString("REMARK");
                String remarkEn = rs.getString("REMARK_EN");
                String callbackUrl = rs.getString("CALLBACK_URL");
                String properties = rs.getString("PROPERTIES");
                String createdBy = rs.getString("CREATED_BY");
                Timestamp createdAt = rs.getTimestamp("CREATED_AT");
                boolean enabled = rs.getBoolean("ENABLED");

                // Tạo đối tượng ConnectorModel và thêm vào map
                ConnectorModel connector = new ConnectorModel(enabled, provider, name, remark, remarkEn, properties, callbackUrl, createdBy, createdAt);
                connectorMap.put(name, connector);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return connectorMap;
    }

    public List<ConnectorModel> reloadConnector() {
        List<ConnectorModel> connectors = new ArrayList<>();
        CallableStatement stmt = null;
        ResultSet resultSet = null;

        try {
            // Sử dụng CallableStatement để gọi store procedure
            stmt = conn.prepareCall("{CALL USP_CONNECTOR_LIST()}");
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
//                Tạo một đối tượng ConnectorModel và gán giá trị từ ResultSet
                ConnectorModel connector = new ConnectorModel();
                connector.setId(resultSet.getLong("ID"));
                connector.setEnabled(resultSet.getInt("ENABLED"));
                connector.setProvider(resultSet.getString("PROVIDER_NAME"));
                connector.setName(resultSet.getString("NAME"));
                connector.setRemark(resultSet.getString("REMARK"));
                connector.setRemarkEn(resultSet.getString("REMARK_EN"));
                connector.setProperties(resultSet.getString("PROPERTIES"));
                connector.setCallbackUrl(resultSet.getString("CALLBACK_URL"));
                connector.setCreatedBy(resultSet.getString("CREATED_BY"));
                connector.setCreatedAt(resultSet.getTimestamp("CREATED_AT")); // Sử dụng Timestamp thay vì Date nếu cần chính xác thời gian

                // Thêm connector vào danh sách
                connectors.add(connector);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng tài nguyên
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connectors;
    }
}
