package com.example.testingProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserAnswerDto {

    @NotNull(message = "Field questionId must not be null")
    private Long questionId;
    private String rightAnswerFreeChoice;
    private Long rightAnswerFourChoiceId;
}
