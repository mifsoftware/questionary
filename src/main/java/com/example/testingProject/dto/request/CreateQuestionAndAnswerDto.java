package com.example.testingProject.dto.request;

import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.dto.request.CreateAnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionAndAnswerDto {

    @Size(min = 5, max = 256, message = "Field question must contain at least 5, maximum 256 characters")
    private String question;
    @NotNull(message = "Field isRight must not be null")
    private QuestionType questionType;
    @Size(min = 1, max = 4, message = "Field answer must contain at least 1, maximum 4 characters")
    private List<CreateAnswerDto> answer;
}
