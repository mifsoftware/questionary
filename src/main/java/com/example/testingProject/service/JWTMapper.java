package com.example.testingProject.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface JWTMapper {

    String create(TokenDetails details);

    TokenDetails parse(String token);

    boolean isValid(String token);


    @Getter
    @AllArgsConstructor
    public class TokenDetails {

        private Long id;
        private String login;
    }
}
