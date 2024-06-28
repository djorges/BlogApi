package org.example.blogapi.controller;

import jakarta.validation.Valid;
import org.example.blogapi.dto.AuthCreateUserRequest;
import org.example.blogapi.dto.AuthLoginRequest;
import org.example.blogapi.dto.AuthResponse;
import org.example.blogapi.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(
        @RequestBody @Valid AuthCreateUserRequest authCreateUserRequest
    ){
        AuthResponse authResponse = userDetailService.createUser(authCreateUserRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(
        @RequestBody @Valid AuthLoginRequest userRequest
    ){
        AuthResponse authResponse = userDetailService.loginUser(userRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
