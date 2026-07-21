package com.afridi.bankmanagementsystem.service;

import com.afridi.bankmanagementsystem.requestdto.LoginRequestDto;
import com.afridi.bankmanagementsystem.requestdto.RegisterRequestDto;
import com.afridi.bankmanagementsystem.responsedto.LoginResponseDto;
import com.afridi.bankmanagementsystem.responsedto.RegisterResponseDto;
import com.afridi.bankmanagementsystem.responsedto.UserResponseDto;

public interface AuthService {
    RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto);

    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);

    UserResponseDto getUserById(Long userId);
}
