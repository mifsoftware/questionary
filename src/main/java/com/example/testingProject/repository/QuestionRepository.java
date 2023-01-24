package com.example.testingProject.repository;

import com.example.testingProject.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    Optional<Question> findById(Long id);
    Long save(Question question);
    Long count();
    List<Question> getAll(List<Long> ids);
}
