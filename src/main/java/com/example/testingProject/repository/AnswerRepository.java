package com.example.testingProject.repository;

import com.example.testingProject.domain.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Answer findById(Long id);
    Long save(Answer answer);
    List<Answer> findByQuestionId(Long questionId);
    Optional<Answer> findRightAnswerByQuestionId(Long questionId);
}
