package com.example.testingProject.service;

import com.example.testingProject.domain.User;
import com.example.testingProject.dto.request.CreateQuestionAndAnswerDto;
import com.example.testingProject.dto.response.QuestionDto;
import com.example.testingProject.security.UserDetailsWithId;

public interface QuestionService {

    void createQuestion(CreateQuestionAndAnswerDto request, User user);
    QuestionDto getQuestion(Long userId);
}
