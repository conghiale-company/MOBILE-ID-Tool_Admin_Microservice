package model;

import java.sql.*;
import java.util.Date;

public class IdentityConnector {
    private Long id;
    private Long enterpriseID;
    private Long connectorID;
    private String enterpriseName;
    private String connectorName;
    private int status;
    private String secretKey;
    private int defaultEnable;
    private String remark;
    private String hmac;
    private String createdBy;
    private Date createdAt;
    private String lastModifiedBy;
    private Date lastModifiedAt;

    public IdentityConnector() {}

    public IdentityConnector(Long id, Long enterpriseID, Long connectorID, int status, String secretKey, int defaultEnable, String remark, String hmac, String createdBy, Date createdAt, String lastModifiedBy, Date lastModifiedAt) {
        this.id = id;
        this.enterpriseID = enterpriseID;
        this.connectorID = connectorID;
        this.status = status;
        this.secretKey = secretKey;
        this.defaultEnable = defaultEnable;
        this.remark = remark;
        this.hmac = hmac;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(Long enterpriseID) {
        this.enterpriseID = enterpriseID;
    }

    public Long getConnectorID() {
        return connectorID;
    }

    public void setConnectorID(Long connectorID) {
        this.connectorID = connectorID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getDefaultEnable() {
        return defaultEnable;
    }

    public void setDefaultEnable(int defaultEnable) {
        this.defaultEnable = defaultEnable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String deleteIConnector(Long enterpriseConnectorID) {
        return null;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getConnectorName() {
        return connectorName;
    }
}
