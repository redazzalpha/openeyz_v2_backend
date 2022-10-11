/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class JwTokenUtils {
    @Autowired
    UserService us;
    private long expiration;
    private Key hmacKey;

    public JwTokenUtils() {
        this.expiration = 1;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String encode(String username, String secret) {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        Users user = us.findById(username).get();
        String jwtToken = Jwts.builder()
                .claim("username", username)
                .claim("role", user.getRoles().get(0).getRoleName())
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();
        expiration = 1;
        return jwtToken;
    }
    public String encode(String secret) {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
       
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwtToken;
    }
    public Jws<Claims> decode(String token, String secret) {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jws = Jwts.parser().setSigningKey(hmacKey).parseClaimsJws(token);
        return jws;
    }
}