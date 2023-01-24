package com.example.testingProject.service.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class AdministratorLoader implements ApplicationRunner {

    @Value("${admin.login}")
    private String login;
    @Value("${admin.password}")
    private String password;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministratorLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if(login.length() < 5 ||  login.length() > 20)
            throw new IllegalArgumentException("Field admin.login must contain at least 5, maximum 20 characters");
        if(password.length() < 6 || password.length() > 20)
            throw new IllegalArgumentException("Field admin.password must contain at least 6, maximum 20 characters");

        if (!StringUtils.hasText(login))
            return;

        var existedUser = userRepository.findByLogin(login);
        if (existedUser.isPresent())
            return;

        var admin = User.builder()
                .password(passwordEncoder.encode(password))
                .login(login)
                .userRoles(UserRoles.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
