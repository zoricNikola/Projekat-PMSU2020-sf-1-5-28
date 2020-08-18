package com.example.projekat_pmsu2020_sf_1_5_28.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rule {

    private static final long serialVersionUID = -7599570197341391702L;
    public enum Operation { MOVE, COPY, DELETE }

    public enum Condition { TO, FROM, CC, SUBJECT }
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("condition")
    @Expose
    private Condition condition;
    @SerializedName("operation")
    @Expose
    private Operation operation;
    @SerializedName("destination")
    @Expose
    private Folder destination;

    public Rule() {}

    public Rule(Long id, String value, Condition condition, Operation operation, Folder destination) {
        super();
        this.id = id;
        this.value = value;
        this.condition = condition;
        this.operation = operation;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Rule.Condition getCondition() {
        return condition;
    }

    public void setCondition(Rule.Condition condition) {
        this.condition = condition;
    }

    public Rule.Operation getOperation() {
        return operation;
    }

    public void setOperation(Rule.Operation operation) {
        this.operation = operation;
    }

    public Folder getDestination() {
        return destination;
    }

    public void setDestination(Folder destination) {
        this.destination = destination;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
