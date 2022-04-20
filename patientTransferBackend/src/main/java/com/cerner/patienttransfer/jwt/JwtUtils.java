package com.cerner.patienttransfer.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Jwt Utility Class
 */
@Slf4j
@Service
public class JwtUtils {

    @Value("${patienttransfer.app.jwtSecret}")
    private String jwtSecret;

    @Value("${patienttransfer.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Function to generate JWT token upon successful authentication
//     * @param userDetails
     * @return encryptedToken
     */
    public String generateJwtToken(UserDetails userDetails){
        String userPrincipal = userDetails.getUsername();

        return Jwts.builder()
            .setSubject((userPrincipal))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expiredL: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
