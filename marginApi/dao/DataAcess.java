package com.example.marginApi.dao;
import com.example.marginApi.model.Identifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class DataAcess {
    private final UserDataAcess userDB;
    private final LocationDataAcess locationDB;

    /**
     * Give acces to multiple DataBase, give and distribute information to the correct class
     */
    @Autowired
    DataAcess(UserDataAcess userDataAcess, LocationDataAcess locationDataAcess){
        this.userDB = userDataAcess;
        this.locationDB = locationDataAcess;
    }


    public UUID addUser(Identifier identifier) {
        UUID uuid = this.userDB.addNewUser(identifier);
        this.locationDB.addLocation("*", uuid, 0);
        return uuid;
    }
}
