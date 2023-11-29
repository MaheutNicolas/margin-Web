package com.example.marginApi.api;

import com.example.marginApi.model.Location;
import com.example.marginApi.service.LocationService;
import com.example.marginApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/location")
@RestController
@CrossOrigin(origins = "https://marginsite.ovh", allowCredentials = "true")
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;

    @Autowired
    public LocationController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @PostMapping(path = "/{name}")
    public void addLocation(@PathVariable("name") String name, @CookieValue(value = "uuid") String uuid){
        this.locationService.addLocation(this.userService.getUser(UUID.fromString(uuid)),name);
    }
    @GetMapping
    public List<Location> getAllLocation(@CookieValue(value = "uuid") String uuid){
        return this.locationService.getAllLocation(this.userService.getUser(UUID.fromString(uuid)));
    }
    @DeleteMapping
    public void removeLocation(@CookieValue(value = "uuid") String uuid){
        this.locationService.removeLocation(this.userService.getUser(UUID.fromString(uuid)));
    }
    @PutMapping(path = "/{name}")
    public void replaceLocationName(@CookieValue(value = "uuid") String uuid, @PathVariable("name") String name){
        this.locationService.replaceNameLocation(this.userService.getUser(UUID.fromString(uuid)), name);
    }
    @GetMapping(path = "/id")
    public int getLocationID(@CookieValue(value = "uuid") String uuid){
        return this.locationService.getLocationID(this.userService.getUser(UUID.fromString(uuid)));
    }
    @PutMapping(path = "/user/{id}")
    public void setLocationID(@CookieValue(value = "uuid") String uuid,@PathVariable("id") int id){
        this.locationService.switchLocation(this.userService.getUser(UUID.fromString(uuid)), id);
    }
}
