package by.dmitry_skachkov.userservice.core.utils;

import by.dmitry_skachkov.userservice.conf.properties.JwtProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenHandler {
    private JwtProperty property;
    private ObjectMapper objectMapper;

    public JwtTokenHandler(JwtProperty property) {
        this.property = property;
    }

    public String generateAccessToken(UserAuth userAuth) {
        try {
            String token = Jwts.builder()
                    .setSubject(objectMapper.writeValueAsString(userAuth))
                    .setIssuer(property.getIssuer())
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(LocalDate.now().plusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS512, property.getSecret())
                    .compact();
            addToContext(userAuth);
            return token;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(); //todo
        }
    }

    public UserAuth getUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();
        try {
            return objectMapper.readValue(claims.getSubject(), UserAuth.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }


    private void addToContext(UserAuth userAuth) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuth, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean validate(String token) {
//        try {
        Jwts.parser().setSigningKey(property.getSecret()).parseClaimsJws(token);
        return true;
//        } catch (SignatureException ex) {
//            TokenError error = new TokenError("Invalid JWT signature");
//            error.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
//            throw error;
//        } catch (MalformedJwtException ex) {
//            TokenError error = new TokenError("Invalid JWT token");
//            error.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
//            throw error;
//        } catch (ExpiredJwtException ex) {
//            TokenError error = new TokenError("Expired JWT token");
//            error.setHttpStatusCode(HttpStatus.FORBIDDEN);
//            throw error;
//        } catch (UnsupportedJwtException ex) {
//            TokenError error = new TokenError("Unsupported JWT token");
//            error.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
//            throw error;
//        } catch (IllegalArgumentException ex) {
//            TokenError error = new TokenError("JWT claims string is empty");
//            error.setHttpStatusCode(HttpStatus.BAD_REQUEST);
//            throw error;
//        }
    } //todo create token Exception class
}
