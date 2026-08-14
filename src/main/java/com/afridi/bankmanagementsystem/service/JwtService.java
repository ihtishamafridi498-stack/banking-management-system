package com.afridi.bankmanagementsystem.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
     String generateToken(Authentication authentication);

     String extractUserName(String token);

     boolean validateToken(String token, UserDetails userDetails);
}
