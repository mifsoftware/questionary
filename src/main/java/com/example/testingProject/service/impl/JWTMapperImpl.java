package com.example.testingProject.service.impl;

import com.example.testingProject.service.JWTMapper;
import com.example.testingProject.util.StringToKeyConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JWTMapperImpl implements JWTMapper {

    long jwtExpiration;
    Key jwtSecret;


    public JWTMapperImpl(
            @Value("${jwt.secret}") String privateJwt,
            @Value("${jwt.expire}") long jwtExpiration
    ) {
        this.jwtExpiration = jwtExpiration;
        jwtSecret = StringToKeyConverter.convertStringToSecretKey(privateJwt);
    }

    @Override
    public String create(TokenDetails details) {
        try {
            return Jwts.builder()
                    .setClaims(tokenDetailToMap(details))
                    .setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                    .signWith(jwtSecret)
                    .compact();

        } catch (InvalidKeyException ex) {
            log.error("invalid signing key", ex);
            throw ex;
        }
    }

    @Override
    public TokenDetails parse(String token) {
        var parser = Jwts.parser()
                .setSigningKey(jwtSecret);
        var claims = parser.parseClaimsJws(token).getBody();
        return mapToTokenDetails(claims);
    }

    @Override
    public boolean isValid(String token) {
        var parser = Jwts.parser()
                .setSigningKey(jwtSecret);

        try {
            var claims = parser.parseClaimsJws(token).getBody();
            mapToTokenDetails(claims);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Map<String, Object> tokenDetailToMap(TokenDetails details) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", details.getId());
        map.put("user_name", details.getLogin());
        return map;
    }

    private TokenDetails mapToTokenDetails(Map<String, Object> map) {
        long id;
        String userName;

        try {
            id = Long.parseLong(String.valueOf(map.get("id")));
        } catch (NumberFormatException ex) {
            log.error("Token Error parsing with id = [{}]", map.get("id"), ex);
            return null;
        }
        try {
            userName = (String) map.get("user_name");
        }
        catch(ClassCastException ex) {
            log.error("Token Error parsing user_name = " + map.get("user_name"));
            return null;
        }

        return new TokenDetails(id, userName);
    }
}
