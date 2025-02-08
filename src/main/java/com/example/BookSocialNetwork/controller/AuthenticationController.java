package com.example.BookSocialNetwork.controller;

import com.example.BookSocialNetwork.model.RegistrationRequest;
import com.example.BookSocialNetwork.service.impl.AuthenticationServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImp service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request){
        service.register(request);
        return ResponseEntity.accepted().build();
    }
}

