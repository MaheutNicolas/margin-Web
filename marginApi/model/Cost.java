package com.example.marginApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cost {
    private boolean variable;
    @JsonProperty("id")
    private int id;
    private String name;
    private int amount;
    private int percent = 0;

    public Cost(@JsonProperty("name") String name, @JsonProperty("amount") int amount, @JsonProperty("variable") boolean variable){
        this.name = name;
        this.amount = amount;
        this.variable = variable;
    }

    public Cost(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("amount") int amount, @JsonProperty("variable") boolean variable, @JsonProperty("percent") int percent){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.variable = variable;
        this.percent = percent;
    }
    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public int getId() {
        return id;
    }

    public int getPercent() {
        return percent;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setId(int id) {
        this.id = id;
    }

    public boolean isCostVariable() {
        return variable;
    }
    public void setCostVariable(boolean variable){
        this.variable = variable;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
