package com.example.testingProject.service;

import com.example.testingProject.dto.request.AuthorizationRequest;
import com.example.testingProject.dto.request.RegistrationRequest;
import com.example.testingProject.dto.response.UserTokenDto;

public interface AuthService {

    void register(RegistrationRequest login);
    UserTokenDto auth(AuthorizationRequest request);
}
