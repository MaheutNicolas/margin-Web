package com.example.marginApi.api;

import com.example.marginApi.model.Recipe;
import com.example.marginApi.service.RecipeService;
import com.example.marginApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/recipe")
@RestController
@CrossOrigin(origins = "https://marginsite.ovh", allowCredentials = "true")
public class RecipeController {

    private final UserService userService;
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(UserService userService, RecipeService recipeService){
        this.userService = userService;
        this.recipeService = recipeService;
    }
    @GetMapping
    public List<Recipe> getAllRecipes(@CookieValue(value = "uuid") String uuid){
        return this.recipeService.getAllRecipes( this.userService.getUser(UUID.fromString(uuid)));
    }
    @PostMapping
    public void addRecipe(@RequestBody @NonNull Recipe recipe, @CookieValue(value = "uuid") String uuid){
        this.recipeService.addRecipe(this.userService.getUser(UUID.fromString(uuid)), recipe);
    }
    @GetMapping(path = "/{recipeId}")
    public Recipe getRecipeById(@CookieValue(value = "uuid") String uuid, @PathVariable("recipeId") int recipeId){
        return this.recipeService.getRecipeById(this.userService.getUser(UUID.fromString(uuid)), recipeId);
    }
    @DeleteMapping(path = "/{recipeId}")
    public void deleteCostById(@CookieValue(value = "uuid") String uuid,  @PathVariable("recipeId") int recipeId ){
        this.recipeService.removeRecipeById(this.userService.getUser(UUID.fromString(uuid)), recipeId);
    }
    @PutMapping
    public void updateCost(@CookieValue(value = "uuid") String uuid, @Validated @NonNull @RequestBody Recipe recipe){
        this.recipeService.updateRecipe(this.userService.getUser(UUID.fromString(uuid)), recipe);
    }
}
