package com.example.testingProject.service;

import com.example.testingProject.domain.User;
import com.example.testingProject.dto.request.CreateUserAnswerDto;
import com.example.testingProject.dto.response.UserAnswerDto;
import com.example.testingProject.security.UserDetailsWithId;

import java.util.List;

public interface UserAnswerService {

    List<UserAnswerDto> getMyAnswer(User user);
    void create(CreateUserAnswerDto request, User user);
}
