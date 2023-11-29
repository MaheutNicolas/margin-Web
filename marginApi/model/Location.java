package com.example.marginApi.model;

public class Location {
    private final String name;
    private final int id;

    public Location(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
