package com.example.testingProject.controller;

import com.example.testingProject.BaseSpringTest;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.dto.request.RegistrationRequest;
import com.example.testingProject.dto.response.UserTokenDto;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseSpringTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository.save(User.builder()
                .login("login")
                .password(passwordEncoder.encode("password"))
                .userRoles(UserRoles.USER)
                .build());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldRegisterUser() {

        var request = RegistrationRequest.builder()
                .login("login1")
                .password("password")
                .build();

        mockMvc.perform(post("/auth/register")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        var user = userRepository.findByLogin("login");
        assertNotNull(user, "user not created");
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldAuthUser() {

        var request = RegistrationRequest.builder()
                .login("login")
                .password("password")
                .build();

        var response = mockMvc.perform(post("/auth")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getStatus(), 200);

        var user = userRepository.findByLogin("login");

        assertNotNull(user, "Пользователь не был создан");
    }
}
