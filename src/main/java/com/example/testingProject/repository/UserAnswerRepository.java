package com.example.testingProject.repository;

import com.example.testingProject.domain.UserAnswer;

import java.util.List;

public interface UserAnswerRepository {
    UserAnswer findById(Long id);
    Long save(UserAnswer answer);
    List<UserAnswer> getMyAnswer(Long id);
    Long getCountUsersStartTesting();
    Long getCountUsersPassTesting();
    Long getCountUsersRightPassTesting();
    Long getCountRightAnswerByUserId(Long id);
    Long getCountUsersBetterMe(Long id, Long myCount);
    Long getCountUsersWorseMe(Long id, Long myCount);
    List<Long> getQuestionsIds(Long userId);
}
