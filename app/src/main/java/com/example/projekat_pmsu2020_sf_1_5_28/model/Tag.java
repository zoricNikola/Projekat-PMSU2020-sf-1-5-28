package com.example.projekat_pmsu2020_sf_1_5_28.model;

public class Tag {
    private String name;
    private int color;

    public Tag() {}

    public Tag(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
