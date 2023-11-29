package com.example.marginApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumberOfSale {
    private final int numberOfSale;

    NumberOfSale(@JsonProperty("numberOfSale") int numberOfSale){
        this.numberOfSale = numberOfSale;
    }
    public int getNumber() {
        return numberOfSale;
    }
}
