package com.example.marginApi.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private int location;
    int numberOfCost;
    int fixedCostTotal = 0;
    private int numberOfRecipe = 0;
    private List<Cost> costList;
    private  List<Cost> fixedCostList = new ArrayList<>();
    private List<Recipe> recipeList ;

    private LocalTime time;

    public User(UUID uuid){
        this.uuid = uuid;
    }
    //--------------- SETTER ----------------

    public void setLocation(int location) {
        this.location = location;
    }

    public void setNumberOfCost(int numberOfCost) {
        this.numberOfCost = numberOfCost;
    }

    public void setFixedCostTotal(int fixedCostTotal) {
        this.fixedCostTotal = fixedCostTotal;
    }

    public void setNumberOfRecipe(int numberOfRecipe) {
        this.numberOfRecipe = numberOfRecipe;
    }

    public void setCostList(List<Cost> costList) {
        this.costList = costList;
    }

    public void setFixedCostList(List<Cost> fixedCostList) {
        this.fixedCostList = fixedCostList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    // --------------  GETTER -------------------------
    public UUID getUuid() {
        return uuid;
    }

    public int getLocation() {
        return location;
    }

    public int getNumberOfCost() {
        return numberOfCost;
    }

    public int getFixedCostTotal() {
        return fixedCostTotal;
    }

    public int getNumberOfRecipe() {
        return numberOfRecipe;
    }

    public List<Cost> getCostList() {
        return costList;
    }

    public List<Cost> getFixedCostList() {
        return fixedCostList;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public LocalTime getTime() {
        return time;
    }

    //------------ INCREMENTS ------------
    // Replace the set( get() ) method

    /**
     * add value directly to the numberOfCost
     * @param value
     */
    public void addNumberOfCost(int value) {
        this.numberOfCost += value;
    }
    /**
     * add value directly to the fixedCostTotal
     * @param value
     */
    public void addFixedCostTotal(int value) {
        this.fixedCostTotal += value;
    }
    /**
     * add value directly to the NumberOfRecipe
     * @param value
     */
    public void addToNumberOfRecipe(int value) {
        this.numberOfRecipe += value;
    }

}
