package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HMACGenerator {

    public static String generateHMAC(String data) {
        try {
            // Khởi tạo MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Chuyển đổi dữ liệu thành mảng byte và băm
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Chuyển đổi kết quả băm thành chuỗi Hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }
}