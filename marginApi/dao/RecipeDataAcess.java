package com.example.marginApi.dao;

import com.example.marginApi.model.Cost;
import com.example.marginApi.model.Recipe;
import com.example.marginApi.model.SQLParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RecipeDataAcess {

    private final SQLParams sql;

    @Autowired
    public RecipeDataAcess(SQLParams sql) {
        this.sql = sql;
    }
    /**
     * save recipe to the recipe table and every cost to the recipe_cost table
     *
     * @param uuid
     * @param recipe
     * @param location
     */
    public void saveRecipe(UUID uuid, Recipe recipe, int location) {
        final String requestIntoRecipe = "INSERT INTO recipe VALUES (?, ?, ?, ?, ?, ?)";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestIntoRecipe);){
            ps.setString(1,uuid.toString());
            ps.setInt(2,recipe.getId());
            ps.setString(3,recipe.getName());
            ps.setInt(4, recipe.getPrice() );
            ps.setInt(5,recipe.getTax());
            ps.setInt(6,location);
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        final String requestIntoCost = "INSERT INTO recipe_cost VALUES (?, ?, ?, ?, ?, ?)";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestIntoCost);){
            for(Cost cost : recipe.getCosts()){
                ps.setString(1,uuid.toString());
                ps.setInt(2,cost.getId());
                ps.setString(3,cost.getName());
                ps.setInt(4, cost.getAmount());
                ps.setInt(5,recipe.getId());
                ps.setInt(6, location);
                ps.addBatch();
            }
        ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * return the list of all the recipe in the database in the java object form.
     * without forgetting to add all the linked cost
     *
     * @param uuid
     * @param location
     * @return
     */
    public List<Recipe> getAllRecipe(UUID uuid, int location) {

        List<Recipe> recipes = new ArrayList<>();

        final String requestIntoRecipe = "SELECT * FROM recipe WHERE uuid = ? AND sales_location = ? ";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(requestIntoRecipe)) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, location);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe(null, 0, null, 0);
                recipe.setId(rs.getInt(2));
                recipe.setName(rs.getString(3));
                recipe.setPrice(rs.getInt(4));
                recipe.setTax(rs.getInt(5));

                final String requestIntoCost = "SELECT * FROM recipe_cost WHERE uuid = ? AND recipe_id = ? AND sales_location = ?";
                try ( final PreparedStatement psToCost = connection.prepareStatement(requestIntoCost)) {
                    psToCost.setString(1, uuid.toString());
                    psToCost.setInt(2, recipe.getId());
                    psToCost.setInt(3, location);
                    ResultSet rsToCost = psToCost.executeQuery();
                    ArrayList<Cost> costs = new ArrayList<>();
                    while (rsToCost.next()) {
                        Cost cost = new Cost(null, 0, false);
                        cost.setId(rsToCost.getInt(2));
                        cost.setName(rsToCost.getString(3));
                        cost.setAmount(rsToCost.getInt(4));
                        costs.add(cost);
                    }
                    recipe.setCosts(costs);
                }
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.out.println("No recipe in DB");
        }
        return recipes;
    }

    /**
     * remove recipe from the recipe table and the cost associate with.
     * Update the other forward recipe to get - 1 in their respective id.
     *
     * @param uuid
     * @param id
     * @param size
     * @param location
     */
    public void removeRecipe(UUID uuid, int id, int size, int location) {
        final String requestDelete = "DELETE FROM recipe WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestDelete);){
            ps.setString(1,uuid.toString());
            ps.setInt(2, id );
            ps.setInt(3, location );
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        final String requestUpdate = "UPDATE recipe SET id = ? WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdate);){
            for(int i = 0; i < size; i++){
                ps.setInt(1, (id+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (id+i+1));
                ps.setInt(3, location );
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        final String requestDeleteFromCost = "DELETE FROM recipe_cost WHERE uuid = ? AND recipe_id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestDeleteFromCost);){
            ps.setString(1,uuid.toString());
            ps.setInt(2, id );
            ps.setInt(3, location );
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        final String requestUpdateToCost = "UPDATE recipe_cost SET recipe_id = ? WHERE uuid = ? AND recipe_id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdateToCost);){
            for(int i = 0; i < size; i++){
                ps.setInt(1, (id+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (id+i+1));
                ps.setInt(3, location );
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Update the recipe by remplace info in the recipe table, delete all the cost linked to the recipe
     * and add the new in the recipe_cost table.
     *
     * @param uuid
     * @param recipe
     * @param location
     */
    public void updateRecipe(UUID uuid, Recipe recipe, int location) {

        final String requestToRecipe = "UPDATE recipe SET name = ?, price = ?, tax = ? WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestToRecipe);){
            ps.setString(1,recipe.getName());
            ps.setInt(2, recipe.getPrice());
            ps.setInt(3, recipe.getTax());
            ps.setString(4, uuid.toString());
            ps.setInt(5, recipe.getId());
            ps.setInt(6, location );
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        final String requestDeleteFromCost = "DELETE FROM recipe_cost WHERE uuid = ? AND recipe_id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestDeleteFromCost);){
            ps.setString(1,uuid.toString());
            ps.setInt(2, recipe.getId());
            ps.setInt(3, location );
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        final String requestIntoCost = "INSERT INTO recipe_cost VALUES (?, ?, ?, ?, ?, ?)";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestIntoCost);){
            for(Cost cost : recipe.getCosts()){
                ps.setString(1,uuid.toString());
                ps.setInt(2,cost.getId());
                ps.setString(3,cost.getName());
                ps.setInt(4, cost.getAmount());
                ps.setInt(5,recipe.getId());
                ps.setInt(6, location );
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
