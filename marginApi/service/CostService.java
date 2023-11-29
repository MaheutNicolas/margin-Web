package com.example.marginApi.service;

import com.example.marginApi.dao.CostDataAcess;
import com.example.marginApi.model.Cost;
import com.example.marginApi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CostService {

    private final CostDataAcess costDataAcess;
    private final RecipeService recipeService;

    @Autowired
    public CostService(CostDataAcess costDataAcess, RecipeService recipeService) {
        this.costDataAcess = costDataAcess;
        this.recipeService = recipeService;
    }


    /**
     * add cost to the CostList and to the fixed cost list after set the id
     */
    public void addCost(User user, Cost cost) {
        cost.setId(user.getNumberOfCost());
        user.addNumberOfCost(1);
        user.getCostList().add(cost);
        if (!cost.isCostVariable()) {
            user.getFixedCostList().add(cost);
            user.addFixedCostTotal(cost.getAmount());
        }
        this.costDataAcess.saveCost(user.getUuid(), cost, user.getLocation());
    }
    /**
     *     remove cost from both list and update the id of every cost
     */
    public void removeCost(User user, int costId){
        Cost cost = getCostByID(user, costId);
        user.getCostList().remove(cost);
        if(!cost.isCostVariable()){
            user.addFixedCostTotal( -(cost.getAmount()) );
        }
        for(int i = costId; i < user.getCostList().size(); i++){
           user.getCostList().get(i).setId(i);
        }
        user.getFixedCostList().clear();
        for (Cost e : user.getCostList()){
            if(!e.isCostVariable()) user.getFixedCostList().add(e);
        }
        user.addNumberOfCost(-1);
        this.costDataAcess.removeCost(user.getUuid(), cost.getId(), user.getCostList().size() - cost.getId(), user.getLocation());
        this.recipeService.removeCost(user, costId);
    }
    /**
    update cost in both the ram and the database.
     */
    public void updateCost(User user, Cost cost) {
        if(!cost.isCostVariable()){
            int previousAmount = getCostByID(user, cost.getId()).getAmount();
            user.addFixedCostTotal(  cost.getAmount() - previousAmount);
            this.recipeService.removeCost(user, cost.getId());
            int fixedPos = getFixedCostPosition(user, cost.getId());
            if(fixedPos != -1) user.getFixedCostList().set(fixedPos, cost);
        }
        user.getCostList().set(cost.getId(), cost);
        this.costDataAcess.updateCost(user.getUuid(), cost, user.getLocation());
        this.recipeService.updateCostInRecipe(user);
    }

    //-------Getter and Setter-------
    public List<Cost> getFixedCostList(User user) {
        return user.getFixedCostList();
    }
    public List<Cost> getVariableCostList(User user) {
        List<Cost> variableCostList = new ArrayList<>();
        variableCostList.addAll(user.getCostList());
        variableCostList.removeAll(user.getFixedCostList());
        return variableCostList;
    }
    public List<Cost> getAllCosts(User user){
        return user.getCostList();
    }
    public Cost getCostByID(User user, int costID){
        for(Cost cost : user.getCostList()){
            if(cost.getId() == costID){
                return cost;
            }
        }
        return null;
    }
    public int getFixedCostPosition(User user, int costID){
        for(int i = 0; i < user.getFixedCostList().size(); i++){
            if(user.getFixedCostList().get(i).getId() == costID){
                return i;
            }
        }
        return -1;
    }

    public int getFixedCostTotal(User user) {
        return user.getFixedCostTotal();
    }
}
