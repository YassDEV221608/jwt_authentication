package com.example.moviesdb.user.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final String SECRET_KEY;
    //used to verify if the user is who he claims to be
    //the minimum required is 256-bit


    public JWTService() {
        this.SECRET_KEY = "feaa46644b6fa4b4d106be6a1e6e3a73c4ada9769c4231f8fe6aa3b4fd6a2deb0dc7552b3ac6a55f9ffa7457758b66ed0387ec9939fa349b8c786b3e08c3fea1";
    }

    public String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    private  <T> T extractClaims(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24*1000*60*365))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails details) {
        final String username = extractUsername(token);
        return username.equals(details.getUsername()) && isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keys = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }
}
