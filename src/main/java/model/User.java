package model;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class User {
    private String userName;
    private String email;
    private String personalCode;
    private String phone;
    private String password;
    private Date passwordExpiredAt;
    private int remainingCounter;
    private int status;
    private int changePassword;
    private int lookEnable;
    private Date lookAt;
    private int verifiedEnable;
    private String forgetKey;
    private String hmac;
    private String createdBy;
    private Date createdAt;
    private String lastModifiedBy;
    private Date lastModifiedAt;

    public User(String userName, String email, String phone, String password, String personalCode, int status, int verifiedEnable) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.personalCode = personalCode;
        this.status = status;
        this.verifiedEnable = verifiedEnable;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getPasswordExpiredAt() {
        return passwordExpiredAt;
    }

    public void setPasswordExpiredAt(Date passwordExpiredAt) {
        this.passwordExpiredAt = passwordExpiredAt;
    }

    public int getRemainingCounter() {
        return remainingCounter;
    }

    public void setRemainingCounter(int remainingCounter) {
        this.remainingCounter = remainingCounter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(int changePassword) {
        this.changePassword = changePassword;
    }

    public int getLookEnable() {
        return lookEnable;
    }

    public void setLookEnable(int lookEnable) {
        this.lookEnable = lookEnable;
    }

    public Date getLookAt() {
        return lookAt;
    }

    public void setLookAt(Date lookAt) {
        this.lookAt = lookAt;
    }

    public int getVerifiedEnable() {
        return verifiedEnable;
    }

    public void setVerifiedEnable(int verifiedEnable) {
        this.verifiedEnable = verifiedEnable;
    }

    public String getForgetKey() {
        return forgetKey;
    }

    public void setForgetKey(String forgetKey) {
        this.forgetKey = forgetKey;
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
}
