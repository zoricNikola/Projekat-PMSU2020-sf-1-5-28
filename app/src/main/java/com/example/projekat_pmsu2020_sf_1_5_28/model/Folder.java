package com.example.projekat_pmsu2020_sf_1_5_28.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Folder implements Serializable {

    private static final long serialVersionUID = 2188634478104553741L;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("numberOfMessages")
    @Expose
    private int numberOfMessages;

    public Folder() {}

    public Folder(Long id, String name, int numberOfMessages) {
        super();
        this.id = id;
        this.name = name;
        this.numberOfMessages = numberOfMessages;
    }

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

    public int getNumberOfMessages() { return numberOfMessages;}

    public void setNumberOfMessages(int numberOfMessages) { this.numberOfMessages = numberOfMessages; }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
