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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class JwTokenUtils {
    @Autowired UserService us;
    // Key is hardcoded here for simplicity. 
    // Ideally this will get loaded from env configuration/secret vault
    @Value("${jwt.secret}")
    private String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    Key hmacKey;

    public JwTokenUtils() {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
    }
    public String encode(String username) {
        Instant now = Instant.now();
        Users user = us.findById(username).get();
        String jwtToken = Jwts.builder()
            .claim("username", username)
            .claim("data", user)
            .setSubject(username)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
            .signWith(SignatureAlgorithm.HS256, hmacKey)
            .compact();

        return jwtToken;
    }
    public Jws<Claims> decode(String token) {
        Key hmacKey1 = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jws = Jwts.parser().setSigningKey(hmacKey1).parseClaimsJws(token);
        return jws;
    }

}
