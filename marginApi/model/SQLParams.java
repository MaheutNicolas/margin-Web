package com.example.marginApi.model;

import org.springframework.stereotype.Component;

@Component
public class SQLParams {
    private final String URL = "jdbc:mysql://localhost:3306/marginapidatabase";
    private final String USER = "root";
    private final String PASSWORD = "***";



    public String getURL() {
        return URL;
    }
    public String getUSER() {
        return USER;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
}
