package com.example.testingProject.dto.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Size;

@Value
@Jacksonized
@Builder
public class AuthorizationRequest {

    @Size(min = 5, max = 20, message = "Field login must contain at least 5, maximum 20 characters")
    String login;
    @Size(min = 6, max = 20, message = "Field password must contain at least 5, maximum 20 characters")
    String password;
}
