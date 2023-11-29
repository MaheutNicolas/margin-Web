package com.example.marginApi.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Recipe {

    private int variableCost = 0;
    private int priceWithoutTax = 0;
    private int marginVariable = 0;
    private List<Cost> costs = new ArrayList<>();
    List<Integer> costsId;
    private int tax;
    private int totalCost = 0;
    private String name;
    private int price;
    private int middleCost = 0;
    private double marginPercent = 0;
    @JsonProperty("id")
    private int id;

    public Recipe(@JsonProperty("name") String name, @JsonProperty("price") int price, @JsonProperty("costs") List<Integer> costs, @JsonProperty("tax") int tax){
        this.name = name;
        this.price = price;
        this.tax = tax;
        if(costs != null){
            this.costsId = costs;
        }
    }

    /**
     * compute all the data to make it to date
     */
    public void setupData() {
        this.totalCost = 0;
        this.variableCost = 0;
        this.priceWithoutTax = price - tax;
        for(Cost cost : costs){
            this.totalCost = this.totalCost + cost.getAmount();
            if(cost.isCostVariable()){
                this.variableCost = this.variableCost + cost.getAmount();
            }
        }
        finaliseSetData();
    }

    /**
     * most generic step to compute data, call in case of small change or after setupData() is call
     */
    private void finaliseSetData(){
        this.marginVariable = priceWithoutTax - totalCost;
        for(Cost cost : costs){
            cost.setPercent(100*cost.getAmount()/totalCost);
        }
        if(costs.size() != 0){
            this.middleCost = totalCost / costs.size();
        }
        this.marginPercent = 100* marginVariable /priceWithoutTax;
    }
    public void configCostList(List<Cost> costList) {
        for( int id : costsId){
            if(costList.get(id).isCostVariable()) costs.add(costList.get(id));
        }
        setupData();
    }
    //------ Getter ----------
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public int getPrice() {
        return price;
    }

    public int getMarginVariable() {
        return marginVariable;
    }

    public int getPriceWithoutTax() {
        return priceWithoutTax;
    }

    public int getTax() {
        return tax;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public int getVariableCost() {
        return variableCost;
    }

    public double getMarginPercent() {
        return marginPercent;
    }

    public int getMiddleCost() {
        return middleCost;
    }
    //--------- Setter -------------

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public void setTax(int tax) {
        this.tax = tax;
        this.priceWithoutTax = this.price - tax;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
        setupData();
    }

    //add 1 cost to the list
    public void addCost(Cost cost){
        this.costs.add(cost);
        this.totalCost = this.totalCost + cost.getAmount();
        if(cost.isCostVariable()){
            this.variableCost = this.variableCost + cost.getAmount();
        }
        finaliseSetData();
    }
    //add multiple Cost to the list
    public void addCost(Cost[] costsToAdd){
        this.costs.addAll(Arrays.asList(costsToAdd));
        for( Cost cost  : costsToAdd ){
            this.totalCost = this.totalCost + cost.getAmount();
            if(cost.isCostVariable()){
                this.variableCost = this.variableCost + cost.getAmount();
            }
            finaliseSetData();
        }

    }
    //remove 1 cost of the list
    public void removeCost( Cost cost ){
        this.costs.remove(cost);
        this.totalCost = this.totalCost - cost.getAmount();
        if(cost.isCostVariable()){
            this.variableCost = this.variableCost - cost.getAmount();
        }
        finaliseSetData();
    }
    //remove multiple cost of the list
    public void removeCost( Cost[] costToRemove) {
        this.costs.removeAll(Arrays.asList(costToRemove));
        for( Cost cost  : costToRemove ){
            this.totalCost = this.totalCost - cost.getAmount();
            if(cost.isCostVariable()){
                this.variableCost = this.variableCost - cost.getAmount();
            }
            finaliseSetData();
        }
    }
}

