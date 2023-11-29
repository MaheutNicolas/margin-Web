package com.example.marginApi.dao;

import com.example.marginApi.model.Identifier;
import com.example.marginApi.model.SQLParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;

@Component
public class UserDataAcess {
    private final SQLParams sql;

    @Autowired
    public UserDataAcess(SQLParams sql) {
        this.sql = sql;
    }
    /**
     * Crypt the password by MD5 for compare purpose with the database
     * @param password
     * @return
     */
    public String hashPassword( String password ){

        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        bytes = md.digest(bytes);
        StringBuilder s = new StringBuilder();
        for (byte b : bytes) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return s.toString();
    }
    /**
     * verify if the user is valid, and return the uuid
     * @param identifier
     * @return
     */
    public UUID getUser(Identifier identifier) {
        //Crypt by SHA256
        String password = hashPassword(identifier.getPassword());
        //compare to DataBase
        final String request = "SELECT * FROM user WHERE name = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, identifier.getName());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(password.equals(rs.getString(2))) {
                return UUID.fromString(rs.getString(3));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    public UUID addNewUser(Identifier identifier){
        UUID uuid = UUID.randomUUID();
        final String request = "INSERT INTO user VALUES (?, ?, ?, ?, ?)";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, identifier.getName());
            ps.setString(2, hashPassword(identifier.getPassword()));
            ps.setString(3, uuid.toString());
            ps.setString(4, identifier.getEmail());
            ps.setInt(5, 0);
            ps.execute();
            return uuid;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    public boolean testUser(UUID id) {
        final String request = "SELECT * FROM user WHERE uuid = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }
        catch (Exception e){
            System.out.println("Test ID in DB echec");
        }
        return false;
    }
    public int getNumberOfSale(UUID id){
        final String request = "SELECT * FROM user WHERE uuid = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(4);
            }
        }
        catch (Exception e){
            System.out.println("get Number of sale in DB echec");
        }
        return 0;
    }

    public void setNumberOfSale(int numberOfSale, UUID id) {
        final String request = "UPDATE user SET number_of_sales = ? WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(request);){
            ps.setInt(1,numberOfSale);
            ps.setString(2, id.toString());
            ps.execute();
        }
        catch (Exception e){
            System.out.println("set Number of sale in DB echec");
        }
    }

    public boolean checkUser(String user) {
        final String request = "SELECT * FROM user WHERE name = ?";
        try (final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
             final PreparedStatement ps = connection.prepareStatement(request);) {
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e);
            return true;
        }
        return true;
    }

    public void deleteUser(String uuid) {

        final String DeleteFromRecipe = "DELETE FROM recipe WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(DeleteFromRecipe);){
            ps.setString(1,uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String DeleteFromCost = "DELETE FROM recipe WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(DeleteFromCost);){
            ps.setString(1,uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String DeleteFromCostRecipe = "DELETE FROM recipe_cost WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(DeleteFromCostRecipe);){
            ps.setString(1,uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String DeleteFromUser = "DELETE FROM user WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(DeleteFromUser);){
            ps.setString(1,uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String DeleteFromLocation = "DELETE FROM sales_location WHERE uuid = ?";
        try(final Connection connection = DriverManager.getConnection(sql.getURL(), sql.getUSER(), sql.getPASSWORD());
            final PreparedStatement ps = connection.prepareStatement(DeleteFromLocation);){
            ps.setString(1,uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
