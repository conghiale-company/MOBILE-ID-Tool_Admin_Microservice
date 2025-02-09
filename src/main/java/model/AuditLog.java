package model;

import java.sql.Date;

public class AuditLog {
    private Long id;
    private int status;
    private Long uri;
    private String requestIP;
    private String request;
    private String response;
    private String hmac;
    private String createBy;
    private Date createAt;

    public AuditLog(){
    }

    public AuditLog(Long id, int status, Long uri, String requestIP, String request, String response, String hmac, String createBy, Date createAt) {
        this.id = id;
        this.status = status;
        this.uri = uri;
        this.requestIP = requestIP;
        this.request = request;
        this.response = response;
        this.hmac = hmac;
        this.createBy = createBy;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUri() {
        return uri;
    }

    public void setUri(Long uri) {
        this.uri = uri;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
