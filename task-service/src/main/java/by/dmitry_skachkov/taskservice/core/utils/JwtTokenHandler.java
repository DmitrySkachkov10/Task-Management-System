package by.dmitry_skachkov.taskservice.core.utils;

import by.dmitry_skachkov.taskservice.conf.properties.JwtProperty;
import by.dmitryskachkov.exception.exceptions.token.TokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
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

    public JwtTokenHandler(JwtProperty property, ObjectMapper objectMapper) {
        this.property = property;
        this.objectMapper = objectMapper;
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
            System.out.println(e.getMessage());
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
        try {
            Jwts.parser().setSigningKey(property.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            TokenException e = new TokenException("Invalid JWT signature");
            e.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
            throw e;
        } catch (MalformedJwtException ex) {
            TokenException e = new TokenException("Invalid JWT token");
            e.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
            throw e;
        } catch (ExpiredJwtException ex) {
            TokenException e = new TokenException("Expired JWT token");
            e.setHttpStatusCode(HttpStatus.FORBIDDEN);
            throw e;
        } catch (UnsupportedJwtException ex) {
            TokenException e = new TokenException("Unsupported JWT token");
            e.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
            throw e;
        } catch (IllegalArgumentException ex) {
            TokenException e = new TokenException("JWT claims string is empty");
            e.setHttpStatusCode(HttpStatus.BAD_REQUEST);
            throw e;
        }
    }
}
