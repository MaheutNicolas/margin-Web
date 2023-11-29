package com.example.marginApi.api;

import com.example.marginApi.dao.DataAcess;
import com.example.marginApi.dao.UserDataAcess;
import com.example.marginApi.model.Identifier;
import com.example.marginApi.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/auth")
@RestController
@CrossOrigin(origins = "https://marginsite.ovh", allowCredentials = "true")
public class AuthController {

    private final UserService userService;

    private final UserDataAcess userDataAcess;

    private final DataAcess dataAcess;

    @Autowired
    public AuthController(UserService userService, UserDataAcess userDataAcess, DataAcess dataAcess) {
        this.userService = userService;
        this.userDataAcess = userDataAcess;
        this.dataAcess = dataAcess;
    }

    @PutMapping
    public boolean authUserByPassword(@RequestBody @NonNull Identifier identifier, HttpServletResponse response){
        UUID uuid = this.userDataAcess.getUser(identifier);
        if(uuid == null) return false;
        this.userService.addUser(uuid);
        ResponseCookie resCookie = ResponseCookie.from("uuid",uuid.toString())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", resCookie.toString());
        return true;
    }

    @PostMapping
    public boolean addUser(@RequestBody @NonNull Identifier identifier, HttpServletResponse response){
        if(!this.userDataAcess.checkUser(identifier.getName())) return false;
        UUID uuid = this.dataAcess.addUser(identifier);
        if(uuid == null) return false;
        userService.addUser(uuid);
        ResponseCookie resCookie = ResponseCookie.from("uuid",uuid.toString())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", resCookie.toString());
        return true;
    }

    @GetMapping
    public boolean verifyUser( @CookieValue(value = "uuid", defaultValue = "null") String uuid, HttpServletRequest request){
        System.out.println(request.getContextPath());
        System.out.println(request.getAuthType());
        System.out.println(request.getHttpServletMapping());
        if(uuid.equals("null")) return false;
        this.userService.addUser(UUID.fromString(uuid));
        return true;
    }
    @DeleteMapping
    public void deleteUser(@CookieValue(value = "uuid", defaultValue = "null") String uuid, HttpServletRequest request, HttpServletResponse response){
        if(uuid.equals("null")) return;
        this.userService.removeUser(UUID.fromString(uuid));
        Cookie cookie = request.getCookies()[0];
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @DeleteMapping(path = "/closeAccount")
    public void deleteUserPermanently(@CookieValue(value = "uuid", defaultValue = "null") String uuid, HttpServletRequest request, HttpServletResponse response){
        if(uuid.equals("null")) return;
        this.userService.removeUser(UUID.fromString(uuid));
        Cookie cookie = request.getCookies()[0];
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        this.userDataAcess.deleteUser(uuid);
    }
    @DeleteMapping(path = "/closeAccount/{uuid}")
    public void deleteUserPermanentlyManually(@RequestParam("uuid") String uuid){
        this.userService.removeUser(UUID.fromString(uuid));
        this.userDataAcess.deleteUser(uuid);
    }
}
