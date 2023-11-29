package com.example.marginApi.service;

import com.example.marginApi.dao.RecipeDataAcess;
import com.example.marginApi.model.Recipe;
import com.example.marginApi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeDataAcess recipeDataAcess;

    @Autowired
    public RecipeService(RecipeDataAcess recipeDataAcess) {
        this.recipeDataAcess = recipeDataAcess;
    }

    //---------Recipe------------

    /**
     * add recipe to the list in ram and to the database after set the id
     *
     * @param recipe
     */
    public void addRecipe(User user, Recipe recipe) {
        recipe.setId(user.getNumberOfRecipe());
        user.addToNumberOfRecipe(+1);
        recipe.configCostList(user.getCostList());
        user.getRecipeList().add(recipe);
        this.recipeDataAcess.saveRecipe(user.getUuid(), recipe, user.getLocation());
    }

    /**
     * remove recipe with the id from the ram and call the database
     * @param id
     */
    public void removeRecipeById(User user, int id) {
        user.getRecipeList().remove(id);
        user.addToNumberOfRecipe(-1);
        for(int i = id; i < user.getRecipeList().size(); i++){
            user.getRecipeList().get(i).setId(i);
        }
        this.recipeDataAcess.removeRecipe(user.getUuid(), id, user.getNumberOfRecipe(), user.getLocation());
    }

    /**
     * update the recipe in the ram and call DB
     *
     * @param recipe
     */
    public void updateRecipe(User user, Recipe recipe) {
        recipe.configCostList(user.getCostList());
        user.getRecipeList().set(recipe.getId(), recipe);
        this.recipeDataAcess.updateRecipe(user.getUuid(), recipe, user.getLocation());
    }
    //------------ RecipeCost ----------

    /**
     * remove cost from every recipe in the ram before call to compute data
     * @param id
     */
    public void removeCost(User user, int id){
        for(Recipe e : user.getRecipeList()){
            e.getCosts().removeIf(cost -> cost.getId() == id);
            e.setupData();
        }
    }

    /**
     * update cost in every recipe in the ram before call to compute data
     */
    public void updateCostInRecipe(User user) {
        user.setRecipeList(recipeDataAcess.getAllRecipe(user.getUuid(), user.getLocation()));
        //TODO verify Method, logique error (newCost in no use)
    }
    //---------Getter and Setter --------------

    public List<Recipe> getAllRecipes(User user) {
        return user.getRecipeList();
    }
    public Recipe getRecipeById(User user, int id){
        return user.getRecipeList().get(id);
    }


}
