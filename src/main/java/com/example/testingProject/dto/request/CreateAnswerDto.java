package com.example.testingProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnswerDto {

    @Size(min = 5, max = 128, message = "Field answer must contain at least 5, maximum 128 characters")
    private String answer;
    @NotNull(message = "Field isRight must not be null")
    private Boolean isRight;
}
