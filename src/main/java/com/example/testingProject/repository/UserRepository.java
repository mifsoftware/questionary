package com.example.testingProject.repository;

import com.example.testingProject.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
    User findById(Long id);
    Long save(User user);

    Long count();

}
