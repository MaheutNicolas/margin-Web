package com.example.marginApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Identifier {

    private final String email;
    private String name;
    private String password;

    public Identifier(@JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty(value = "email", defaultValue = "null") String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
