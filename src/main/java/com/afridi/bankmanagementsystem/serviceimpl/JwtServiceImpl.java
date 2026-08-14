package com.afridi.bankmanagementsystem.serviceimpl;

import com.afridi.bankmanagementsystem.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

        @Value("${jwt.secret}")
        private String secret;

        @Override
        public String generateToken(Authentication authentication) {

            Map<String, Object> claims = new HashMap<>();

            String username = authentication.getName();

            return Jwts.builder()
                    .claims(claims)
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 100 * 60 * 30))
                    .signWith(getKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractUserName(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        private <T> T extractClaim(
                String token,
                Function<Claims, T> claimResolver) {

            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {

            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

        public boolean validateToken(
                String token,
                UserDetails userDetails) {

            final String userName = extractUserName(token);

            return userName.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        private SecretKey getKey() {

            byte[] keyBytes = Decoders.BASE64.decode(secret);

            return Keys.hmacShaKeyFor(keyBytes);
        }
}
