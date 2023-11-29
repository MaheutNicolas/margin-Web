package com.example.marginApi.service;

import com.example.marginApi.dao.*;
import com.example.marginApi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class UserService {

    private final CostDataAcess costDataAcess;
    private final LocationDataAcess locationDataAcess;
    private final RecipeDataAcess recipeDataAcess;
    private final UserDataAcess userDataAcess;
    private final MainService mainService;

    private final List<User> users = new ArrayList<>();
    Thread clock;

    @Autowired
    public UserService(CostDataAcess costDataAcess, DataAcess dataAcess, LocationDataAcess locationDataAcess, RecipeDataAcess recipeDataAcess, UserDataAcess userDataAcess, MainService mainService){
        this.costDataAcess = costDataAcess;
        this.mainService = mainService;
        this.locationDataAcess = locationDataAcess;
        this.recipeDataAcess = recipeDataAcess;
        this.userDataAcess = userDataAcess;
        //Thread to check if user continue to pass instruction, if not, remove it from the users list
        this.clock = new Thread(() -> {
            try {
                while (true) {
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LocalTime now = LocalTime.now();
                    for(int i = 0; i < users.size(); i ++){
                         int comparator = now.compareTo(users.get(i).getTime().plusMinutes(5));
                         if(comparator < 0){
                             users.remove(users.get(i));
                             System.out.println("Users logout");
                         }
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }, "Demon");

        this.clock.setDaemon(true);
        this.clock.start();
    }
    public User getUser(UUID id){
        for(User user : this.users){
            if(user.getUuid().equals(id)){
                user.setTime(LocalTime.now());
                return user;
            }
        }
        if(this.userDataAcess.testUser(id)){
            User user = new User(id);
            this.users.add(user);
            return user;
        }
        return null;
    }
    public void addUser(UUID id){
        for(User user : this.users){
            if(user.getUuid().equals(id)){
                return;
            }
        }
        User user = new User(id);
        this.users.add(user);
        this.mainService.loadFromSave(user);
        System.out.println("User "+ id+" is auth");
    }
    public void removeUser(UUID id) {
        if(this.users.isEmpty()){
            System.out.println("No users");
            return;
        }
        this.users.removeIf(user -> user.getUuid().equals(id));
        System.out.println("user "+ id + " log out");
    }
}
