package model;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ConnectorModel {
    private Long id;
    private int enabled; // 0 = INACTIVE, 1 = ACTIVE
    private String provider;
    private String name;
    private String remark;
    private String remarkEn;
    private String Properties;
    private String callbackUrl;
    private String hmac;
    private String createdBy;
    private Date createdAt;
    private String lastModifiedBy;
    private Date lastModifiedAt;

    public ConnectorModel() {
    }

    public ConnectorModel(String provider, String remark, String remarkEn) {
        this.provider = provider;
        this.remark = remark;
        this.remarkEn = remarkEn;
    }

    public ConnectorModel(boolean enabled, String provider, String name, String remark, String remarkEn, String properties, String callbackUrl, String createdBy, Timestamp createdAt) {
        this.enabled = (enabled) ? 1 : 0;
        this.provider = provider;
        this.name = name;
        this.remark = remark;
        this.remarkEn = remarkEn;
        this.Properties = properties;
        this.callbackUrl = callbackUrl;
        this.createdBy = createdBy;
        this.createdAt = new Date(createdAt.getTime());
    }

    // Getters and Setters
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    public String getProperties() {
        return Properties;
    }

    public void setProperties(String Properties) {
        this.Properties = Properties;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int status) {
        this.enabled = enabled;
    }

    public String getStatusAsString() {
        return (enabled == 1) ? "ACTIVE" : "INACTIVE";
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
}
