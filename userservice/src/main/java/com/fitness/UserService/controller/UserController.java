package com.fitness.UserService.controller;

import com.fitness.UserService.dto.RegisterRequest;
import com.fitness.UserService.dto.UserResponse;
import com.fitness.UserService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
        return  ResponseEntity.ok(userService.getUserProfile(userId));

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> resgister(@Valid @RequestBody RegisterRequest registerRequest){
        return  ResponseEntity.ok(userService.register(registerRequest));

    }
}
