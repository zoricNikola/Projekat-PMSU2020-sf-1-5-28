package com.example.projekat_pmsu2020_sf_1_5_28.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachment implements Serializable {

    private static final long serialVersionUID = 5112510341538929038L;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("name")
    @Expose
    private String name;

    public Attachment() {}

    public Attachment(Long id, String data, String mimeType, String name) {
        super();
        this.id = id;
        this.data = data;
        this.mimeType = mimeType;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
