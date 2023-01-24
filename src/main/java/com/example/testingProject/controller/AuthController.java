package com.example.testingProject.controller;

import com.example.testingProject.dto.request.AuthorizationRequest;
import com.example.testingProject.dto.request.RegistrationRequest;
import com.example.testingProject.dto.response.UserTokenDto;
import com.example.testingProject.exception.GeneralExceptionHandler;
import com.example.testingProject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/auth", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {

    AuthService authService;

    /**
     * User registration
     */
    @Operation(summary = "User registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping("register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@RequestBody @Valid RegistrationRequest body) {
        authService.register(body);
    }

    /**
     * User authorization
     */
    @Operation(summary = "User authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping
    public UserTokenDto auth(@RequestBody @Valid AuthorizationRequest body) {
        return authService.auth(body);
    }

}