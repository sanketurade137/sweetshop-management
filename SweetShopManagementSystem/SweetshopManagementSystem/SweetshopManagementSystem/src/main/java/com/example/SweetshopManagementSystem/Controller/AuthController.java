package com.example.SweetshopManagementSystem.Controller;

import com.example.SweetshopManagementSystem.Entity.User;
import com.example.SweetshopManagementSystem.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody User user){
        user.setRole(user.getRole().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
         return ResponseEntity.ok(userService.login(user));
    }

}
