package com.example.marginApi.dao;

import com.example.marginApi.model.Location;
import com.example.marginApi.model.SQLParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class LocationDataAcess {
    private final SQLParams sql;

    @Autowired
    public LocationDataAcess(SQLParams sql) {
        this.sql = sql;
    }
    public int getSavedLocation(UUID uuid) {
        final String request = "SELECT * FROM user WHERE uuid = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("sales_location");
        }
        catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }
    public void setLocation(int locationID, UUID uuid) {
        final String requestUpdate = "UPDATE user SET sales_location = ? WHERE uuid = ? ";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdate)){
            ps.setInt(1,locationID);
            ps.setString(2,uuid.toString());
            ps.execute();
        }
        catch (Exception e){
            System.out.println("set location in userDB : echec");
        }
    }
    public int addLocation(String name, UUID uuid) {
        int length = getLocationsLength(uuid);
        return addLocation(name, uuid, length);
    }
    public int addLocation(String name, UUID uuid, int location) {
        final String request = "INSERT INTO sales_location VALUES (?, ?, ?)";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(request)){
            ps.setInt(1, location);
            ps.setString(2, name);
            ps.setString(3, uuid.toString());
            ps.execute();
        }
        catch (SQLException e) {
            System.out.println("add location in locationDB : echec");
        }
        return location;
    }

    public void removeLocation(int locationID, UUID uuid) {
        if(getLocationsLength(uuid) <= 1 ) return;
        final String request = "DELETE FROM sales_location WHERE uuid = ? AND id = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(request)){
            ps.setString(1, uuid.toString());
            ps.setInt(2, locationID);
            ps.execute();
        } catch (SQLException e) {
            System.out.println("remove location in locationDB : echec");
        }

        int length = getLocationsLength(uuid);

        final String requestUpdate = "UPDATE sales_location SET id = ? WHERE uuid = ? AND id = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdate);){
            for(int i = 0; i < length - locationID; i++){
                ps.setInt(1, (locationID+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (locationID+i+1));
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        final String requestUpdateToCost = "UPDATE cost SET sales_location = ? WHERE uuid = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdateToCost);){
            for(int i = 0; i < length - locationID; i++){
                ps.setInt(1, (locationID+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (locationID+i+1));
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        final String requestUpdateToRecipe = "UPDATE recipe SET sales_location = ? WHERE uuid = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdateToRecipe);){
            for(int i = 0; i < length - locationID; i++){
                ps.setInt(1, (locationID+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (locationID+i+1));
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        final String requestUpdateToCostRecipe = "UPDATE recipe_cost SET sales_location = ? WHERE uuid = ? AND sales_location = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestUpdateToCostRecipe);){
            for(int i = 0; i < length - locationID; i++){
                ps.setInt(1, (locationID+i));
                ps.setString(2, uuid.toString());
                ps.setInt(3, (locationID+i+1));
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Location> getAllLocation(UUID uuid) {
        ArrayList<Location> locations = new ArrayList<>();
        final String request = "SELECT * FROM sales_location WHERE uuid = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Location location = new Location(rs.getString("name"), rs.getInt("id"));
                locations.add(location);
            }
            return locations;
        }
        catch (Exception e){
            System.out.println("get ALL location in locationDB : echec");
        }
        return new ArrayList<>();
    }
    public int getLocationsLength(UUID uuid){
        int length = 0;
        final String requestID = "SELECT * FROM sales_location WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestID)){
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                length++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return length;
    }

    public void replaceLocationName(String name, UUID uuid, int location) {
        final String requestID = "UPDATE sales_location SET name = ? WHERE uuid = ? AND id = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(requestID)){
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.setInt(3, location);
            ps.execute();
        } catch (SQLException e) {
            System.out.println("update location's name in locationDB : echec");
        }
    }
}
