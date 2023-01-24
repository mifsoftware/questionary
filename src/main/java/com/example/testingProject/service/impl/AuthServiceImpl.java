package com.example.testingProject.service.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.dto.request.AuthorizationRequest;
import com.example.testingProject.dto.request.RegistrationRequest;
import com.example.testingProject.dto.response.UserDto;
import com.example.testingProject.dto.response.UserTokenDto;
import com.example.testingProject.exception.BaseRuntimeException;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.service.AuthService;
import com.example.testingProject.service.JWTMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JWTMapper jwtMapper;
    UserToTokenDetailsMapper userToTokenDetailsMapper;

    @Override
    public void register(RegistrationRequest request) {
        var isExists = userRepository.findByLogin(request.getLogin()).isPresent();
        if (isExists) {
            log.warn("user already exists {}", request);
            throw new BaseRuntimeException("Login already taken.");
        }

        User user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRoles(UserRoles.USER)
                .build();

        userRepository.save(user);
        log.info("save user {}", user);
    }

    @Override
    public UserTokenDto auth(AuthorizationRequest request) {
        var user = userRepository.findByLoginAndPassword(request.getLogin(), passwordEncoder.encode(request.getPassword()))
                .orElseThrow(() -> {
                    log.warn("user not exists {}", request);
                    return new BaseRuntimeException("User is not found");
                });

        return build(user);
    }

    private UserTokenDto build(User user) {
        var token = jwtMapper.create(userToTokenDetailsMapper.toTokenDetails(user));
        return new UserTokenDto(new UserDto(user.getLogin(), user.getId()), token);
    }
}
