package view.modal;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/10
 * Time: 5:09 PM
 */

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class ConfigInfo {
    private static final Logger LOGGER = Logger.getLogger(ConfigInfo.class);

    private String hostname;
    private String dbName;
    private String userName;
    private String password;

//    Instance initialization
    private volatile static ConfigInfo configInfo;
    private ConfigInfo() throws IOException {
        readConfigFile();
    }
    public static ConfigInfo getInstance() throws IOException {
        if (configInfo == null) {
            synchronized (ConfigInfo.class) {
                if (configInfo == null) {
                    configInfo = new ConfigInfo();
                }
            }
        }

        return configInfo;
    }

    public ConfigInfo(String hostname, String dbName, String userName, String password) {
        this.hostname = hostname;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void readConfigFile() throws IOException {
        String configFilePath = "src/main/resources/config.cfg";
        Properties properties = new Properties();

        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            properties.load(reader);

            this.hostname = properties.getProperty("HOST_NAME");
            this.dbName = properties.getProperty("DB_NAME");
            this.userName = properties.getProperty("USER_NAME");
            this.password = properties.getProperty("PASSWORD");

//            Check for null or missing values
            if (this.hostname == null || this.hostname.isEmpty()) {
                LOGGER.warn("HOST_NAME is missing or empty in the configuration file.");
            }
            if (this.dbName == null || this.dbName.isEmpty()) {
                LOGGER.warn("DB_NAME is missing or empty in the configuration file.");
            }
            if (this.userName == null || this.userName.isEmpty()) {
                LOGGER.warn("USER_NAME is missing or empty in the configuration file.");
            }
            if (this.password == null || this.password.isEmpty()) {
                LOGGER.warn("PASSWORD is missing or empty in the configuration file.");
            }

        } catch (IOException e) {
            LOGGER.error("[READ CONFIGURATION FILE ERROR] Error reading the configuration file: " + e.getMessage());
            throw e;
        }
    }
}
