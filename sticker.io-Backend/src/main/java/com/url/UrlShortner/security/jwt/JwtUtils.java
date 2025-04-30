package com.url.UrlShortner.security.jwt;

import com.url.UrlShortner.service.UserDetailsImp;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class JwtUtils {

    /*
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    // Authorization -> Bearer <TOKEN>
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    //extract jwt token from http header
 */
    @Value("${jwt.secret}")
    private String jwtsecret;
    @Value("${jwt.expiration}")
    private Long jwtexpiration;

    public String extractJwtTokenFromHeader(HttpServletRequest request) {
        String bearertoken = request.getHeader("Authorization");
        if (bearertoken != null && bearertoken.startsWith("Bearer ")) {
            return bearertoken.substring(7);
        }
        return null;
    }

    //generating jwt toke it has three parts header payload and signature
    //generating jwt token
    public String generateJwtToken(UserDetailsImp userdetails) {
        String username = userdetails.getUsername();
        List<String> roles = userdetails.getAuthorities().stream()
                .map(authority -> {
                    String role = authority.getAuthority();
                    // Ensure roles are prefixed with ROLE_ (Spring Security expects this)
                    return role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
                })
                .collect(Collectors.toList());
        return Jwts.builder()
                .subject(username)
                .claim("authorities", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtexpiration)))
                .signWith(keys())
                .compact();
    }

    private Key keys() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtsecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) keys())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) keys())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

  /*
    public String generateToken(UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} */

