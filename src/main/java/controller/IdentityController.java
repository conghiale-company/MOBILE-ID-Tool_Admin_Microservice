package controller;

import DAO.IdentityDAO;
import model.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.util.List;

import static database.MySQLZNSConnUtils.getMySQLConnection;

public class IdentityController {
    private static final Logger LOGGER = Logger.getLogger(IdentityController.class);

    public IdentityController(MainView mainView) {
    }

    /**
     * [GET ALL IDENTITIES]
     * Retrieve a list of all identities from the DAO and return it to the client.
     *
     * This method calls the `getAllIdentities` method from the IdentityDAO class to retrieve
     * all identities from the database and returns the list of identities as the response.
     * The result is wrapped in a response object.
     *
     * @return A response object containing the list of identities or an error message if the
     *         retrieval process fails.
     */
    public static List<Identity> getAllIdentities() throws Exception {
        List<Identity> identities = new ArrayList<>();

        ResultSet resultSet = IdentityDAO.getInstance().getAllIdentities();
        if (resultSet != null) {
            while (resultSet.next()) {
                Identity identity = new Identity();
                identity.setId(resultSet.getLong("ID"));
                identity.setEnabled(resultSet.getByte("ENABLED"));
                identity.setName(resultSet.getString("NAME"));
                identity.setRemark_en(resultSet.getString("REMARK_EN"));
                identity.setRemark(resultSet.getString("REMARK"));
                identity.setClientID(resultSet.getString("CLIENT_ID"));
                identity.setClientSecret(resultSet.getString("CLIENT_SECRET"));
                identity.setProperties(resultSet.getString("PROPERTIES"));

                identities.add(identity);
            }
            LOGGER.warn("[SUCCESS CALL PROCEDURE] All IDENTITY retrieved SUCCESSFULLY");
        } else {
            LOGGER.warn("[CALL PROCEDURE] Call procedure USP_IDENTITY_LIST return data is NULL");
        }

        return identities;
    }

    /**
     * [GET IDENTITY BY ID]
     * Retrieve an identity by its ID from the DAO and return the details.
     *
     * This method calls the `getIdentityByID` method from the IdentityDAO class to retrieve
     * the details of an identity by its ID and returns the identity details as the response.
     *
     * @param id The ID of the identity to retrieve.
     * @return A response object containing the identity details or an error message if the
     *         retrieval process fails.
     */
    public static Identity getIdentityByID(long id) throws Exception {
        Identity identity = null;
        ResultSet resultSet = IdentityDAO.getInstance().getIdentityByID(id);

        if (resultSet != null) {
            if (resultSet.next()) {
                identity = new Identity();
                identity.setId(resultSet.getLong("ID"));
                identity.setEnabled(resultSet.getInt("ENABLED"));
                identity.setName(resultSet.getString("NAME"));
                identity.setRemark_en(resultSet.getString("REMARK_EN"));
                identity.setRemark(resultSet.getString("REMARK"));
                identity.setClientID(resultSet.getString("CLIENT_ID"));
                identity.setClientSecret(resultSet.getString("CLIENT_SECRET"));

//                Blob logoBlob = resultSet.getBlob("LOGO");
//                if (logoBlob != null) {
//                    byte[] logoBytes = logoBlob.getBytes(1, (int) logoBlob.length());
//                    String base64Logo = new String(logoBytes);
//                    identity.setLogoBase64(base64Logo);
//                }

                identity.setProperties(resultSet.getString("PROPERTIES"));
                identity.setCreatedBy(resultSet.getString("CREATED_BY"));
                identity.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
                identity.setModifiedBy(resultSet.getString("MODIFIED_BY"));
                identity.setModifiedAt(resultSet.getTimestamp("MODIFIED_AT"));
            }
        } else {
            LOGGER.warn("[CALL PROCEDURE] Call procedure USP_IDENTITY_GET_DETAIL return data is NULL with IDENTITY_ID: " + id);
        }

        return identity;
    }

    /**
     * [INSERT IDENTITY]
     * Insert a new identity using the DAO and return the created identity ID.
     *
     * This method calls the `insertIdentity` method from the IdentityDAO class to insert
     * a new identity and returns the generated identity ID as the response.
     *
     * @param name The name of the identity.
     * @param remarkEn The English remark for the identity.
     * @param remark The remark for the identity.
     * @param clientID The client ID associated with the identity.
     * @param clientSecret The client secret associated with the identity.
     * @param properties Additional properties related to the identity.
     * @param createdBy The user who created the identity.
     * @return A response object containing the ID of the created identity or an error message if the insert fails.
     */
    public static long insertIdentity(String name, String remarkEn, String remark, String clientID, String clientSecret,
                                      String properties, String createdBy) throws Exception {
//        Call DAO addIdentity
        return IdentityDAO.getInstance().insertIdentity(name, remarkEn, remark, clientID, clientSecret, properties, createdBy);
    }

    /**
     * [UPDATE IDENTITY]
     * Update an existing identity using the DAO and return the response code.
     *
     * This method calls the `updateIdentity` method from the IdentityDAO class to update
     * an existing identity and returns the response code indicating the result of the operation.
     *
     * @param id The ID of the identity to update.
     * @param remarkEn The English remark for the identity.
     * @param remark The remark for the identity.
     * @param properties The properties related to the identity.
     * @param modifiedBy The user who is modifying the identity.
     * @return A response object containing the result of the update operation.
     */
    public static String updateIdentity(long id, String remarkEn, String remark, String properties, String modifiedBy) throws Exception {
        return IdentityDAO.getInstance().updateIdentity(id, remarkEn, remark, properties, modifiedBy);
    }

    /**
     * [DELETE IDENTITY]
     * Delete or enable/disable an identity using the DAO and return the response code.
     *
     * This method calls the `deleteIdentity` method from the IdentityDAO class to delete
     * or enable/disable an identity based on the provided enable status and returns
     * the response code indicating the result of the operation.
     *
     * @param id The ID of the identity to delete.
     * @param enable The enable status (0 = disable, 1 = enable).
     * @param modifiedBy The user who is modifying the identity.
     * @return A response object containing the result of the delete operation.
     */
    public static String deleteIdentity(long id, int enable, String modifiedBy) throws Exception {
        return IdentityDAO.getInstance().deleteIdentity(id, enable, modifiedBy);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}"); // Cho phép số điện thoại 10-15 chữ số
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"); // Định dạng email hợp lệ
    }

    public String generateAppId() {
        java.util.Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(now);

        // Tạo số random 5 chữ số
        Random random = new Random();
        int randomNumber = random.nextInt(100000); // Tạo số từ 0 đến 99999
        String formattedRandomNumber = String.format("%05d", randomNumber); // Đảm bảo đủ 5 chữ số

        // Ghép chuỗi
        return timestamp + formattedRandomNumber;
    }

//    HAS BEEN REPLACED --> view.Modal.AddNewIdentityModal AddNewIdentityModal
    public void fillTextFields(Identity identity, JLabel lblLogoThumbnail) {
//        mainView.txtIdentityName.setText(identity.getName());
//        int businessType = identity.getbusinessType();
//        if (businessType == 1) {
//            mainView.rbOrganizational.setSelected(true);
//            mainView.rbIndividual.setSelected(false);
//        } else {
//            mainView.rbOrganizational.setSelected(false);
//            mainView.rbIndividual.setSelected(true);
//        }
//        mainView.OwnerName.getModel().setSelectedItem(identity.getOwnerName());
//        mainView.txtIdentityClientID.setText(identity.getClientID());
//        mainView.txtIdentityClientSecret.setText(identity.getClientSecret());
//        mainView.txtIdentityPhone.setText(identity.getPhone());
//        mainView.txtIdentityEmail.setText(identity.getEmail());
//
//        // Decode Base64 logo to display image
//        String base64Logo = identity.getLogoBase64();
//        if (base64Logo != null && !base64Logo.isEmpty()) {
//            try {
//                byte[] logoBytes = Base64.getDecoder().decode(base64Logo);
//                ImageIcon logoIcon = new ImageIcon(logoBytes);
//                Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//                lblLogoThumbnail.setIcon(new ImageIcon(logoImage));
//            } catch (IllegalArgumentException e) {
//                lblLogoThumbnail.setText("Invalid logo data");
//                e.printStackTrace();
//            }
//        }
//
//        mainView.txtIdentityHmac.setText(identity.getHmac());
//        mainView.txtIdentityCreatedBy.setText(identity.getCreatedBy());
    }

//    HAS BEEN REPLACED --> view.MainView loadAllIdentity
    public void refreshIdentityTable() throws Exception {
//        Xóa toàn bộ dữ liệu cũ trong bảng
//        mainView.identityTableModel.setRowCount(0);
//
////        Lấy danh sách doanh nghiệp từ cơ sở dữ liệu
//        List<Identity> identities = IdentityDAO.getInstance().getAllIdentities();
//        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
//
////        Thêm lại dữ liệu mới từ danh sách
//        for (Identity identity : identities) {
//            mainView.identityTableModel.addRow(new Object[]{
//                    identity.getId(),
//                    identity.getEnabled() == 1 ? "ACTIVE" : "INACTIVE",
//                    identity.getName(),
//                    identity.getbusinessType() == 1 ? "ORGANIZATIONAL" : "INDIVIDUAL",
//                    identity.getOwnerName(),
//                    identity.getClientID(),
//                    identity.getClientSecret(),
//                    identity.getPhone(),
//                    identity.getEmail(),
//                    identity.getLogo(),
//                    identity.getHmac(),
//                    identity.getCreatedBy(),
//                    identity.getCreatedAt() != null ? dateFormat.format(identity.getCreatedAt()) : null,
//            });
//        }
    }

    public String encodeFileToBase64(String filePath) {
        // Kiểm tra nếu filePath là null hoặc rỗng, hoặc file không tồn tại
        if (filePath == null || filePath.trim().isEmpty()) {
            return null; // Nếu không chọn logo, trả về null
        }

        File file = new File(filePath);

        // Kiểm tra nếu file không tồn tại hoặc không phải là file hợp lệ
        if (!file.exists() || !file.isFile()) {
            return null; // Nếu file không hợp lệ, trả về null
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            return Base64.getEncoder().encodeToString(fileBytes);  // Convert to Base64
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getClientKey() {
//        String apiUrl = ""; // URL của API WSO2
//        String clientId = "iSXdLwt1zuK2QcT2DYUmelEngr4a";                // Client ID để gọi API (nếu có)
//        String clientSecret = "kQDGiZ22_LQBFy8kGXnX17Uu_S0a";        // Client Secret để gọi API (nếu có)
//        String grantType = "client_credentials";           // Grant type (tuỳ thuộc vào yêu cầu API WSO2)
//
//        try {
//            // Tạo URL connection
//            URL url = new URL(apiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
//
//            // Tham số gửi lên API
//            String postData = "grant_type=" + URLEncoder.encode(grantType, "UTF-8");
//
//            // Gửi dữ liệu
//            conn.setDoOutput(true);
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(postData.getBytes());
//                os.flush();
//            }
//
//            // Đọc phản hồi từ API
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // Phân tích kết quả JSON trả về
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        response.append(line);
//                    }
//
//                    // Parse JSON để lấy ClientID và ClientSecret
//                    JSONObject jsonResponse = new JSONObject(response.toString());
//                    String receivedClientId = jsonResponse.getString("client_id");
//                    String receivedClientSecret = jsonResponse.getString("client_secret");
//
//                    // Chèn vào các trường trong giao diện
//                    mainView.txtIdentityClientID.setText(receivedClientId);
//                    mainView.txtIdentityClientSecret.setText(receivedClientSecret);
//
//                    JOptionPane.showMessageDialog(null, "Client Key retrieved successfully!");
//                }
//            } else {
//                // Xử lý lỗi
//                JOptionPane.showMessageDialog(null, "Failed to retrieve client key. Response code: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error occurred while retrieving client key: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }
}