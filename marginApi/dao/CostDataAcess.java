package com.example.marginApi.dao;

import com.example.marginApi.model.Cost;
import com.example.marginApi.model.SQLParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CostDataAcess {

    private final SQLParams sql;

    @Autowired
    public CostDataAcess(SQLParams sql) {
        this.sql = sql;
    }

    /**
     * Save cost in the database
     */
    public void saveCost(UUID uuid, Cost cost, int location)  {
        final String request = "INSERT INTO cost VALUES (?, ?, ?, ?, ?, ?)";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(request);){
            ps.setString(1,uuid.toString());
            ps.setInt(2,cost.getId());
            ps.setString(3,cost.getName());
            ps.setInt(4, cost.getAmount() );
            ps.setBoolean(5,cost.isCostVariable());
            ps.setInt(6, location);
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * return to the form of a list all cost in the cost table
     *
     * @param uuid
     * @param location
     * @return
     */
    public List<Cost> getAllCost(UUID uuid, int location){
        ArrayList<Cost> costs = new ArrayList<>();
        final String request = "SELECT * FROM cost WHERE uuid = ? AND sales_location = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, location);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Cost cost = new Cost(null, 0, false);
                cost.setId(rs.getInt(2));
                cost.setName(rs.getString(3));
                cost.setAmount(rs.getInt(4));
                cost.setCostVariable(rs.getBoolean(5));
                costs.add(cost);
            }
        }
        catch (Exception e){
            System.out.println("No cost in DB");
        }
        return costs;
    }

    /**
     * update the cost in the cost table, and if the cost is variable -> update in the recipe_cost table
     * if the cost is fixed -> delete it from all recipe in the recipe_cost table
     *
     * @param uuid
     * @param cost
     * @param location
     */
    public void updateCost(UUID uuid, Cost cost, int location){
        final String request = "UPDATE cost SET name = ?, amount = ?, isVariable = ? WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(request);){
            ps.setString(1,cost.getName());
            ps.setInt(2, cost.getAmount() );
            ps.setBoolean(3,cost.isCostVariable());
            ps.setString(4, uuid.toString());
            ps.setInt(5, cost.getId());
            ps.setInt(6,location);
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        if(cost.isCostVariable()){
            final String requestToRecipe = "UPDATE recipe_cost SET name = ?, amount = ? WHERE uuid = ? AND id = ? AND sales_location = ?";
            try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
                final PreparedStatement ps = connection.prepareStatement(requestToRecipe);){
                ps.setString(1,cost.getName());
                ps.setInt(2, cost.getAmount() );
                ps.setString(3, uuid.toString());
                ps.setInt(4, cost.getId());
                ps.setInt(5, location);
                ps.execute();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        else {
            final String requestDeleteToRecipe = "DELETE FROM recipe_cost WHERE uuid = ? AND id = ? AND sales_location = ?";
            try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
                final PreparedStatement ps = connection.prepareStatement(requestDeleteToRecipe);){
                ps.setString(1,uuid.toString());
                ps.setInt(2,cost.getId());
                ps.setInt(3,location);
                ps.execute();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * remove cost from the cost table, subtract 1 to the id of the forward cost
     * and delete from all the recipe in the recipe_cost table
     *
     * @param uuid
     * @param id
     * @param size
     * @param location
     */
    public void removeCost(UUID uuid, int id, int size, int location){

        final String requestDelete = "DELETE FROM cost WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestDelete);){
            ps.setString(1,uuid.toString());
            ps.setInt(2, id);
            ps.setInt(3, location);
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        final String requestUpdate = "UPDATE cost SET id = ? WHERE uuid = ? AND id = ? AND sales_location = ?";

        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdate);){
            for(int i = 0; i < size; i++){
                ps.setInt(1, (id+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (id+i+1));
                ps.setInt(4, location);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        final String requestDeleteToRecipe = "DELETE FROM recipe_cost WHERE uuid = ? AND id = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestDeleteToRecipe);){
            ps.setString(1,uuid.toString());
            ps.setInt(2, id);
            ps.setInt(3, location);
            ps.execute();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
