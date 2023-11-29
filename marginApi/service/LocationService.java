package com.example.marginApi.service;
import com.example.marginApi.dao.LocationDataAcess;
import com.example.marginApi.model.Location;
import com.example.marginApi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationDataAcess locationDataAcess;
    private final MainService mainService;

    @Autowired
    public LocationService(LocationDataAcess locationDataAcess, MainService mainService) {
        this.locationDataAcess = locationDataAcess;
        this.mainService = mainService;
    }

    public void addLocation(User user, String name) {
        user.setLocation(this.locationDataAcess.addLocation(name, user.getUuid()));
    }
    public void switchLocation(User user, int locationID){
        user.setLocation(locationID);
        this.locationDataAcess.setLocation(locationID, user.getUuid());
        this.mainService.loadFromSave(user);
    }
    public void removeLocation(User user){
        this.locationDataAcess.removeLocation(user.getLocation(), user.getUuid());
        user.setLocation(0);
        this.mainService.loadFromSave(user);
    }
    public List<Location> getAllLocation(User user){
        return this.locationDataAcess.getAllLocation(user.getUuid());
    }
    public void replaceNameLocation(User user, String name) {
        this.locationDataAcess.replaceLocationName(name, user.getUuid(), user.getLocation());
    }
    public int getLocationID(User user) {
        return user.getLocation();
    }
}
