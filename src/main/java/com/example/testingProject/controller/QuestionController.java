package com.example.testingProject.controller;

import com.example.testingProject.dto.request.CreateQuestionAndAnswerDto;
import com.example.testingProject.dto.response.QuestionDto;
import com.example.testingProject.exception.GeneralExceptionHandler;
import com.example.testingProject.exception.UserNotFoundException;
import com.example.testingProject.security.AuthenticatedUser;
import com.example.testingProject.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/question", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuestionController {
    QuestionService questionService;
    AuthenticatedUser authenticationUser;

    /**
     * Create a question
     */
    @Operation(summary = "Create a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
    })
    @Secured("ROLE_ADMIN")
    @PostMapping("create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody @Valid CreateQuestionAndAnswerDto request) {
        var user = authenticationUser.getWithThrow();
        questionService.createQuestion(request, user);
    }

    /**
     * Get a question
     */
    @Operation(summary = "Get a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
    })
    @GetMapping("get_question")
    public QuestionDto get() {
        var user = authenticationUser.getWithThrow();
        return questionService.getQuestion(user.getId());
    }
}
