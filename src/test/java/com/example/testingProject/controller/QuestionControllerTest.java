package com.example.testingProject.controller;

import com.example.testingProject.BaseSpringTest;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.dto.request.AuthorizationRequest;
import com.example.testingProject.dto.request.CreateAnswerDto;
import com.example.testingProject.dto.request.CreateQuestionAndAnswerDto;
import com.example.testingProject.repository.AnswerRepository;
import com.example.testingProject.repository.QuestionRepository;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.service.AuthService;
import com.example.testingProject.service.JWTMapper;
import com.example.testingProject.service.impl.UserToTokenDetailsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest extends BaseSpringTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private String adminToken;

    @Value("${admin.login}")
    private String adminLogin;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JWTMapper jwtMapper;
    @Autowired
    private UserToTokenDetailsMapper userToTokenDetailsMapper;
    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AuthService authService;

    @BeforeEach
    @Transactional
    public void init() {
        userRepository.save(User.builder()
                .login("login")
                .password(passwordEncoder.encode("login"))
                .userRoles(UserRoles.USER)
                .build());

        User admin = userRepository.findByLogin(adminLogin).get();

        adminToken = jwtMapper.create(userToTokenDetailsMapper.toTokenDetails(admin));
    }

    @Test
    @SneakyThrows
    public void shouldCreateQuestion() {

        var request = CreateQuestionAndAnswerDto.builder()
                .question("When did the French Revolution end?")
                .questionType(QuestionType.FREECHOICE)
                .answer(List.of(CreateAnswerDto.builder()
                                .answer("1799")
                                .isRight(true)
                        .build()))
                .build();

        mockMvc.perform(post("/question/create")
                        .content(mapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        var question = questionRepository.getAll(List.of());
        var answer = answerRepository.findByQuestionId(question.get(0).getId());

        assertNotNull(question, "question not created");
        assertNotNull(answer, "answer not created");
        assertEquals(question.get(0).getQuestion(), request.getQuestion());
        assertEquals(question.get(0).getQuestionType(), request.getQuestionType());
        assertEquals(answer.get(0).getAnswer(), request.getAnswer().get(0).getAnswer());
        assertEquals(answer.get(0).getIsRight(), request.getAnswer().get(0).getIsRight());
    }

    @Test
    @SneakyThrows
    public void shouldGetQuestion() {

        var request = CreateQuestionAndAnswerDto.builder()
                .question("When did the French Revolution end?")
                .questionType(QuestionType.FREECHOICE)
                .answer(List.of(CreateAnswerDto.builder()
                        .answer("1799")
                        .isRight(true)
                        .build()))
                .build();

        mockMvc.perform(post("/question/create")
                        .content(mapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/question/get_question")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(request.getQuestion()))
                .andExpect(jsonPath("$.questionType").value(request.getQuestionType().name()))
                .andExpect(jsonPath("$.answers").isNotEmpty())
                .andReturn();
    }
}
