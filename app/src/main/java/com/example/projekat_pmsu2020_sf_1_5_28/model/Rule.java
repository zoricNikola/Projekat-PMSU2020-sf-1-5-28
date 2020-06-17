package com.example.projekat_pmsu2020_sf_1_5_28.model;

public class Rule {

    private Condition condition;
    private Operation operation;

    private Folder destination;
    private String value;

    public Rule() {}

    public Rule(Condition condition, Operation operation, Folder destination, String value) {
        this.condition = condition;
        this.operation = operation;
        this.destination = destination;
        this.value = value;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Folder getDestination() {
        return destination;
    }

    public void setDestination(Folder destination) {
        this.destination = destination;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Operation {
        MOVE,
        COPY,
        DELETE
    }

    public enum Condition {
        TO,
        FROM,
        CC,
        SUBJECT
    }
}
