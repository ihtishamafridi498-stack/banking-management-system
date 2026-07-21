package com.afridi.bankmanagementsystem.controller;

import com.afridi.bankmanagementsystem.requestdto.LoginRequestDto;
import com.afridi.bankmanagementsystem.requestdto.RegisterRequestDto;
import com.afridi.bankmanagementsystem.responsedto.LoginResponseDto;
import com.afridi.bankmanagementsystem.responsedto.RegisterResponseDto;
import com.afridi.bankmanagementsystem.responsedto.UserResponseDto;
import com.afridi.bankmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        RegisterResponseDto responseDto=authService.registerUser(registerRequestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto responseDto=authService.loginUser(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(authService.getUserById(userId));
    }
}
