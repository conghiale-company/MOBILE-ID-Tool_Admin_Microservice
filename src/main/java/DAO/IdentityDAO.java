package DAO;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/07
 * Time: 4:35 PM
 */

import database.MySQLZNSConnUtils;
import org.apache.log4j.Logger;

import java.sql.*;

import static database.MySQLZNSConnUtils.getMySQLConnection;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class IdentityDAO {
    private static final Logger LOGGER = Logger.getLogger(IdentityDAO.class);

    private static Connection conn;

//    Instance initialization
    private volatile static IdentityDAO identityDAO;
    private IdentityDAO() {}
    public static IdentityDAO getInstance() throws Exception {
        if (identityDAO == null) {
            synchronized (IdentityDAO.class) {
                if (identityDAO == null) {
                    identityDAO = new IdentityDAO();
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

        return identityDAO;
    }

    /**
     * [GET ALL IDENTITIES]
     * Retrieve all identities from the database.
     *
     * This method calls the stored procedure `USP_IDENTITY_LIST` to retrieve a
     * list of identities from the database. The result is returned as a ResultSet.
     *
     * @return A ResultSet containing the list of identities retrieved from the database.
     * @throws RuntimeException If an error occurs during the execution of the stored procedure.
     */
    public ResultSet getAllIdentities() {
        String storedProcedure = "{CALL USP_IDENTITY_LIST()}";

        try {
            CallableStatement callableStatement = conn.prepareCall(storedProcedure);
            return callableStatement.executeQuery();
        } catch (Exception e) {
            LOGGER.error("[ERROR CALL PROCEDURE] Call procedure USP_IDENTITY_LIST is FAILED - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * [GET IDENTITY BY ID]
     * Retrieve identity details by ID from the database.
     *
     * This method calls the stored procedure `USP_IDENTITY_GET_DETAIL` to retrieve
     * the details of an identity based on the provided ID. The procedure accepts an
     * input parameter for the identity ID and returns a ResultSet if the identity
     * exists. It also provides a response code to indicate the success or failure of the operation.
     *
     * @param identityID The ID of the identity to retrieve.
     * @return A ResultSet containing the details of the identity, or null if the identity is not found.
     * @throws Exception If an error occurs during the execution of the stored procedure.
     *
     * Procedure behavior:
     * - Input parameter: `identityID` (Long) specifies the ID of the identity.
     * - Output parameter: A response code (`String`) indicating:
     *   - "0" if the procedure executes successfully.
     *   - Non-"0" if the identity does not exist or is disabled.
     * - Logs success or failure messages based on the outcome of the call.
     */
    public ResultSet getIdentityByID(Long identityID) throws Exception {
        try {
            CallableStatement stmt = conn.prepareCall("{CALL USP_IDENTITY_GET_DETAIL(?, ?)}");

//            Set value for input parameter
            stmt.setLong(1, identityID);

//            Register output parameters
            stmt.registerOutParameter(2, Types.VARCHAR);

//            Execute procedure
            boolean hasResultSet = stmt.execute();

//            Get output parameter value
            String responseCode = stmt.getString(2);

            if (responseCode.equals("0")) {
                if (hasResultSet) {
                    LOGGER.info("[SUCCESS CALL PROCEDURE USP_IDENTITY_GET_DETAIL] New IDENTITY with ID " + identityID + " was retrieved successfully");
                    return stmt.getResultSet();
                } else {
                    LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_GET_DETAIL] Cannot execute call procedure with ID " + identityID);
                }
            } else {
                LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_GET_DETAIL] No IDENTITY found with ID " + identityID + " or IDENTITY is disabled");
            }
        } catch (Exception e) {
            LOGGER.error("[ERROR CALL PROCEDURE] Call procedure USP_IDENTITY_GET_DETAIL is FAILED - " + e.getMessage());
            throw new Exception(e);
        }
        return null;
    }

    /**
     * [INSERT IDENTITY]
     * Insert a new identity into the database.
     *
     * This method calls the stored procedure `USP_IDENTITY_INSERT` to create a new identity
     * record in the database. It accepts various details about the identity, sets them as
     * input parameters, and retrieves output values including the response code and the newly
     * created identity ID.
     *
     * @param name The name of the identity.
     * @param remark_en The English remark associated with the identity.
     * @param remark The remark associated with the identity.
     * @param clientID The client ID associated with the identity.
     * @param clientSecret The client secret associated with the identity.
     * @param properties Additional properties of the identity in a serialized format.
     * @param createdBy The user who created the identity.
     * @return The ID of the newly inserted identity, or `0` if the insertion fails.
     * @throws SQLException If an error occurs during the execution of the stored procedure.
     *
     * Procedure behavior:
     * - Input parameters:
     *   1. `name` (String): The name of the identity.
     *   2. `remark_en` (String): The English remark.
     *   3. `remark` (String): The remark.
     *   4. `clientID` (String): The client ID.
     *   5. `clientSecret` (String): The client secret.
     *   6. `properties` (String): Additional properties.
     *   7. `createdBy` (String): The creator's identifier.
     * - Output parameters:
     *   8. `responseCode` (String): A code indicating the status of the operation:
     *      - "0": Success.
     *      - "1001": Name is null.
     *      - "1002": Identity with the same name already exists and is active.
     *   9. `identityId` (Long): The ID of the newly created identity.
     * - Logs success or failure messages based on the response code.
     */
    public long insertIdentity(String name, String remark_en, String remark, String clientID, String clientSecret,
                                 String properties, String createdBy) throws SQLException {
        String responseCode;
        long identityId; // Variable to store the IDENTITY_ID value from the stored procedure

        try (CallableStatement stmt = conn.prepareCall("{CALL USP_IDENTITY_INSERT(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
//            Set input parameters
            stmt.setString(1, name);
            stmt.setString(2, remark_en);
            stmt.setString(3, remark);
            stmt.setString(4, clientID);
            stmt.setString(5, clientSecret);
            stmt.setString(6, properties);
            stmt.setString(7, createdBy);

//            Register output parameters
            stmt.registerOutParameter(8, Types.VARCHAR); // pRESPONSE_CODE
            stmt.registerOutParameter(9, Types.BIGINT);  // pIDENTITY_ID

//            Execute stored procedure
            stmt.execute();

//            Get values from output parameters
            responseCode = stmt.getString(8);  // Get the RESPONSE CODE value
            identityId = stmt.getLong(9);     // Get the IDENTITY_ID value

            if (responseCode != null) {
                switch (responseCode) {
                    case "0":
                        LOGGER.info("[SUCCESS CALL PROCEDURE USP_IDENTITY_INSERT] New IDENTITY with name " + name + " has been INSERT SUCCESSFULLY");
                        return identityId;
                    case "1001":
                        LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_INSERT] The name value of IDENTITY is NULL");
                        break;
                    case "1002":
                        LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_INSERT] An IDENTITY with name " + name + " already exists and is being activated.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return identityId;
    }

    /**
     * [UPDATE IDENTITY]
     * Update the details of an existing identity in the database.
     *
     * This method calls the stored procedure `USP_IDENTITY_UPDATE` to update the remarks,
     * properties, and modification details of an identity based on its ID.
     *
     * @param id The unique identifier of the identity to be updated.
     * @param remarkEn The new English remark for the identity.
     * @param remark The new remark for the identity.
     * @param properties Additional properties of the identity in a serialized format.
     * @param modifiedBy The user who is modifying the identity.
     * @return A response code indicating the result of the update operation:
     *         - "0": Update successful.
     *         - "1003": Identity not found with the provided ID.
     *         - `null`: The stored procedure returned a null response.
     * @throws SQLException If an error occurs during the execution of the stored procedure.
     *
     * Procedure behavior:
     * - Input parameters:
     *   1. `id` (Long): The unique identifier of the identity.
     *   2. `remarkEn` (String): The new English remark.
     *   3. `remark` (String): The new remark.
     *   4. `properties` (String): Additional properties.
     *   5. `modifiedBy` (String): The modifier's identifier.
     * - Output parameter:
     *   6. `responseCode` (String): A code indicating the operation status:
     *      - "0": Update successful.
     *      - "1003": Identity not found with the provided ID.
     *      - `null`: Response code is null.
     *
     * Logs:
     * - Logs a success message if the update is successful.
     * - Logs a warning if the identity is not found or if the response code is null.
     */
    public String updateIdentity(long id, String remarkEn, String remark, String properties, String modifiedBy) throws SQLException {
        CallableStatement stmt;
        String responseCode;

        try {
//            Chuẩn bị stored procedure call
            stmt = conn.prepareCall("{CALL USP_IDENTITY_UPDATE(?, ?, ?, ?, ?, ?)}");

//            Set input parameters
            stmt.setLong(1, id);
            stmt.setString(2, remarkEn);
            stmt.setString(3, remark);
            stmt.setString(4, properties);
            stmt.setString(5, modifiedBy);

//            Register output parameter
            stmt.registerOutParameter(6, Types.VARCHAR); // pRESPONSE_CODE

//            Execute the stored procedure
            stmt.execute();

//            Get output parameter value
            responseCode = stmt.getString(6);

            if (responseCode != null) {
                switch (responseCode) {
                    case "0":
                        LOGGER.info("[SUCCESS CALL PROCEDURE USP_IDENTITY_UPDATE] IDENTITY with ID " + id + " has been UPDATED SUCCESSFULLY");
                    case "1003":
                        LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_UPDATE] IDENTITY not found with ID " + id);
                        break;
                }
            } else {
                LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_UPDATE] The name value of RESPONSE_CODE is NULL");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return responseCode;
    }

    /**
     * [DELETE IDENTITY]
     * Update the status of an identity in the database to enable or disable it.
     *
     * This method calls the stored procedure `USP_IDENTITY_UPDATE_STATUS` to update the status
     * of an identity based on its ID, effectively enabling or disabling it.
     *
     * @param id The unique identifier of the identity to be updated.
     * @param enable The status to set for the identity:
     *               - `1` to enable the identity.
     *               - `0` to disable the identity.
     * @param modifiedBy The user who is modifying the identity status.
     * @return A response code indicating the result of the operation:
     *         - "0": Status update successful.
     *         - "1003": Identity not found with the provided ID.
     *         - `null`: The stored procedure returned a null response.
     * @throws SQLException If an error occurs during the execution of the stored procedure.
     *
     * Procedure behavior:
     * - Input parameters:
     *   1. `id` (Long): The unique identifier of the identity.
     *   2. `enable` (Integer): The new status of the identity (`1` for enabled, `0` for disabled).
     *   3. `modifiedBy` (String): The modifier's identifier.
     * - Output parameter:
     *   4. `responseCode` (String): A code indicating the operation status:
     *      - "0": Status update successful.
     *      - "1003": Identity not found with the provided ID.
     *      - `null`: Response code is null.
     *
     * Logs:
     * - Logs a success message if the status update is successful.
     * - Logs a warning if the identity is not found or if the response code is null.
     * - Logs any SQL exceptions encountered during the execution.
     */
    public String deleteIdentity(long id, int enable, String modifiedBy) {
        String responseCode;
        try (CallableStatement stmt = conn.prepareCall("{CALL USP_IDENTITY_UPDATE_STATUS(?, ?, ?, ?)}")) {
            stmt.setLong(1, id);
            stmt.setInt(2, enable);
            stmt.setString(3, modifiedBy);

            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            responseCode = stmt.getString(2);

            if (responseCode != null) {
                switch (responseCode) {
                    case "0":
                        LOGGER.info("[SUCCESS CALL PROCEDURE USP_IDENTITY_UPDATE_STATUS] IDENTITY with ID " + id + " has been DELETED SUCCESSFULLY");
                    case "1003":
                        LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_UPDATE_STATUS] IDENTITY not found with ID " + id);
                        break;
                }
            } else {
                LOGGER.warn("[CALL PROCEDURE USP_IDENTITY_UPDATE_STATUS] The name value of RESPONSE_CODE is NULL");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            responseCode = "0";
        }

        return responseCode;
    }
}
