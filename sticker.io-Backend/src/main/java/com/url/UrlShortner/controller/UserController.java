package com.url.UrlShortner.controller;

import com.url.UrlShortner.Model.User;
import com.url.UrlShortner.Repository.UserRepo;
import com.url.UrlShortner.dtos.LoginRequest;
import com.url.UrlShortner.dtos.RegisterRequest;
import com.url.UrlShortner.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://projectex.xyz")
@RestController
@RequestMapping("/api/auth")

public class UserController {

    public  UserService service;
    public UserRepo repo;

    public UserController(UserService service, UserRepo repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> loginRequest(@RequestBody LoginRequest loginRequest)
    {

        return ResponseEntity.ok(service.authenticateUser(loginRequest));
    }
    //registering the user
    @PostMapping("/public/register")
    public ResponseEntity<?> userRegister(@RequestBody RegisterRequest request)
    {
        User user=new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        service.saveuser(user);
        return ResponseEntity.ok("user registered successfully");
    }
}
