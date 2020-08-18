package com.example.projekat_pmsu2020_sf_1_5_28.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = -4719862163497779925L;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("smtpAddress")
    @Expose
    private String smtpAddress;
    @SerializedName("smtpPort")
    @Expose
    private Integer smtpPort;
    @SerializedName("inServerType")
    @Expose
    private Short inServerType;
    @SerializedName("inServerAddress")
    @Expose
    private String inServerAddress;
    @SerializedName("inServerPort")
    @Expose
    private Integer inServerPort;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    public Account() {}

    public Account(Long id, String smtpAddress, Integer smtpPort, Short inServerType, String inServerAddress,
                   Integer inServerPort, String username, String password, String displayName) {
        super();
        this.id = id;
        this.smtpAddress = smtpAddress;
        this.smtpPort = smtpPort;
        this.inServerType = inServerType;
        this.inServerAddress = inServerAddress;
        this.inServerPort = inServerPort;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public Short getInServerType() {
        return inServerType;
    }

    public void setInServerType(Short inServerType) {
        this.inServerType = inServerType;
    }

    public String getInServerAddress() {
        return inServerAddress;
    }

    public void setInServerAddress(String inServerAddress) {
        this.inServerAddress = inServerAddress;
    }

    public Integer getInServerPort() {
        return inServerPort;
    }

    public void setInServerPort(Integer inServerPort) {
        this.inServerPort = inServerPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
