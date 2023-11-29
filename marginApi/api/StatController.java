package com.example.marginApi.api;

import com.example.marginApi.model.Stat;
import com.example.marginApi.service.MainService;
import com.example.marginApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/stat")
@RestController
@CrossOrigin(origins = "https://marginsite.ovh", allowCredentials = "true")
public class StatController {
    private final MainService mainService;
    private final UserService userService;

    @Autowired
    public StatController(MainService mainService, UserService userService){
        this.mainService = mainService;
        this.userService = userService;
    }

    @GetMapping
    public Stat getAllStat(@CookieValue(value = "uuid") String uuid){
        return mainService.getAllStat(userService.getUser(UUID.fromString(uuid)));
    }

}
