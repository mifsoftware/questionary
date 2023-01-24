package com.example.testingProject.controller;

import com.example.testingProject.dto.request.CreateUserAnswerDto;
import com.example.testingProject.dto.response.UserAnswerDto;
import com.example.testingProject.exception.UserNotFoundException;
import com.example.testingProject.security.AuthenticatedUser;
import com.example.testingProject.service.UserAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/user_answer", produces = APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAnswerController {
    private final UserAnswerService userAnswerService;
    private final AuthenticatedUser authenticatedUser;

    /**
     * Getting my answers
     */
    @Operation(summary = "Getting my answers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @GetMapping
    public List<UserAnswerDto> getMyAnswer() {
        var user = authenticatedUser.getWithThrow();
        return userAnswerService.getMyAnswer(user);
    }

    /**
     * Create an answer to a question
     */
    @Operation(summary = "Create an answer to a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
    })
    @PostMapping("create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody @Valid CreateUserAnswerDto request) {
        var authUser = authenticatedUser.getWithThrow();
        userAnswerService.create(request, authUser);
    }
}
