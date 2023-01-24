package com.example.testingProject.dto.response;

import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.dto.response.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String question;
    private QuestionType questionType;
    private List<AnswerDto> answers;
}
