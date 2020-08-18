package com.example.projekat_pmsu2020_sf_1_5_28.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TokenDTO implements Serializable {
    private static final long serialVersionUID = -6241;

    @SerializedName("jwt")
    @Expose
    private String jwt;
    @SerializedName("user")
    @Expose
    private User user;

    public TokenDTO() {}

    public TokenDTO(String token, User user) {
        this.jwt = token;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
