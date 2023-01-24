package com.example.testingProject.controller;

import com.example.testingProject.BaseSpringTest;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.service.JWTMapper;
import com.example.testingProject.service.impl.UserToTokenDetailsMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticControllerTest extends BaseSpringTest {

    @Value("${admin.login}")
    private String adminLogin;

    private String adminToken;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JWTMapper jwtMapper;
    @Autowired
    private UserToTokenDetailsMapper userToTokenDetailsMapper;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository.save(User.builder()
                .login("login")
                .password(passwordEncoder.encode("password"))
                .userRoles(UserRoles.USER)
                .build());

        User admin = userRepository.findByLogin(adminLogin).get();

        adminToken = jwtMapper.create(userToTokenDetailsMapper.toTokenDetails(admin));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldGetMyStatistic() {
        mockMvc.perform(get("/statistic/own")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passPercentage").value("0.0%"))
                .andExpect(jsonPath("$.betterMe").value("0%"))
                .andExpect(jsonPath("$.worseMe").value("0%"))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldGetAllStatistic() {

        var usersCount = userRepository.count();

        mockMvc.perform(get("/statistic/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allUsers").value(usersCount))
                .andExpect(jsonPath("$.startTesting").value(0L))
                .andExpect(jsonPath("$.passTesting").value(0L))
                .andExpect(jsonPath("$.rightPassTesting").value(0L))
                .andReturn();
    }
}
