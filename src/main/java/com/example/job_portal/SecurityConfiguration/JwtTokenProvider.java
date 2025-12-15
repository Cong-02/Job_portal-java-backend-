package com.example.job_portal.SecurityConfiguration;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private final PrivateKey privateKey = keyPair.getPrivate();
    private final PublicKey publicKey = keyPair.getPublic();

    private final long Expiration_Time = 3600000;

    public String ganerateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Expiration_Time))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    //    // lay token tu header
    public String resolveToken(HttpServletRequest request) {
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            return headerToken.substring(7);
        }
        return null;
    }

}
