package com.example.marginApi.model;

import java.util.ArrayList;
import java.util.List;

public class Stat {

    private int fixedCostTotal = 0;
    private final List<Recipe> marginRanking = new ArrayList<>();
    private List<Recipe> percentRanking = new ArrayList<>();
    private double marginVariableAverage = 0;
    private int incomeThreshold = 0;
    private int averageTicket = 0;
    private double sellToThreshold = 0;

    public Stat(List<Recipe> recipes, int fixedCostTotal){

        if(recipes.size() == 0) return;
        this.fixedCostTotal = fixedCostTotal;
        for(Recipe recipe : recipes){
            this.marginVariableAverage = recipe.getMarginPercent();
        }
        this.marginVariableAverage = this.marginVariableAverage/recipes.size();
        this.incomeThreshold = (int) Math.ceil(fixedCostTotal / (marginVariableAverage*0.01));

        //rank Recipe
        this.averageTicket = 0;
        this.marginRanking.addAll(recipes);
        this.percentRanking.addAll(recipes);

        for( int ref = 0; ref < recipes.size(); ref++){
            this.averageTicket += recipes.get(ref).getPriceWithoutTax();
            for(int i = 0; i < recipes.size() - ref -1; i++){
                if(marginRanking.get(i).getMarginVariable() < marginRanking.get(i+1).getMarginVariable() ){
                    Recipe mem = marginRanking.get(i);
                    marginRanking.set(i,marginRanking.get(i+1));
                    marginRanking.set(i+1, mem);
                }
                if(percentRanking.get(i).getMarginPercent() < percentRanking.get(i+1).getMarginPercent() ){
                    Recipe mem = percentRanking.get(i);
                    percentRanking.set(i,percentRanking.get(i+1));
                    percentRanking.set(i+1, mem);
                }
            }
        }
        this.averageTicket = this.averageTicket / recipes.size();
        this.sellToThreshold = Math.ceil(this.incomeThreshold / this.averageTicket);
    }

    public int getFixedCostTotal() {
        return fixedCostTotal;
    }

    public int getIncomeThreshold() {
        return incomeThreshold;
    }

    public double getMarginVariableAverage() {
        return marginVariableAverage;
    }

    public List<Recipe> getMarginRanking() {
        return marginRanking;
    }

    public List<Recipe> getPercentRanking() {
        return percentRanking;
    }

    public void setPercentRanking(List<Recipe> percentRanking) {
        this.percentRanking = percentRanking;
    }

    public int getAverageTicket() {
        return averageTicket;
    }

    public double getSellToThreshold() {
        return sellToThreshold;
    }
}
