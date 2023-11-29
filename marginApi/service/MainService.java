package com.example.marginApi.service;

import com.example.marginApi.dao.CostDataAcess;
import com.example.marginApi.dao.LocationDataAcess;
import com.example.marginApi.dao.RecipeDataAcess;
import com.example.marginApi.model.Cost;
import com.example.marginApi.model.Stat;
import com.example.marginApi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MainService {


    private final LocationDataAcess locationDataAcess;
    private final CostDataAcess costDataAcess;
    private final RecipeDataAcess recipeDataAcess;
    private final CostService costService;
    private final RecipeService recipeService;

    @Autowired
    public MainService(LocationDataAcess locationDataAcess, CostDataAcess costDataAcess, RecipeDataAcess recipeDataAcess, CostService costService, RecipeService recipeService) {
        this.locationDataAcess = locationDataAcess;
        this.costDataAcess = costDataAcess;
        this.recipeDataAcess = recipeDataAcess;
        this.costService = costService;
        this.recipeService = recipeService;
    }


    public void loadFromSave(User user) {
        UUID id = user.getUuid();
        user.setLocation(this.locationDataAcess.getSavedLocation(id));
        user.setCostList(this.costDataAcess.getAllCost(id, user.getLocation()));
        for( Cost e : user.getCostList()){
            if(!e.isCostVariable()){
                user.getFixedCostList().add(e);
                user.setFixedCostTotal( user.getFixedCostTotal() + e.getAmount());
            }
        }
        user.setNumberOfCost(user.getCostList().size());
        user.setRecipeList(this.recipeDataAcess.getAllRecipe(id, user.getLocation()));
        user.setNumberOfRecipe(user.getRecipeList().size());
    }
    public Stat getAllStat(User user) {
        return new Stat(this.recipeService.getAllRecipes(user), this.costService.getFixedCostTotal(user));
    }

}