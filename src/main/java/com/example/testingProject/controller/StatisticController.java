package com.example.testingProject.controller;

import com.example.testingProject.dto.response.MyStatisticDto;
import com.example.testingProject.dto.response.StatisticDto;
import com.example.testingProject.exception.GeneralExceptionHandler;
import com.example.testingProject.security.AuthenticatedUser;
import com.example.testingProject.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/statistic", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    StatisticService statisticService;
    AuthenticatedUser authenticatedUser;

    /**
     * Getting user statistics
     */
    @Operation(summary = "Getting user statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
    })
    @Secured("ROLE_ADMIN")
    @GetMapping("all")
    public StatisticDto allStatistic() {
        return statisticService.getAllStatistic();
    }

    /**
     * Getting my statistics
     */
    @Operation(summary = "Getting my statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = GeneralExceptionHandler.ErrorResponse.class))),
    })
    @GetMapping("own")
    public MyStatisticDto myStatistic() {
        var user = authenticatedUser.getWithThrow();
        return statisticService.getMyStatistic(user);
    }
}
