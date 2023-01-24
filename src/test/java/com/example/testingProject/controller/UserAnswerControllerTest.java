package com.example.testingProject.controller;

import com.example.testingProject.BaseSpringTest;
import com.example.testingProject.domain.Answer;
import com.example.testingProject.domain.Question;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.dto.request.AuthorizationRequest;
import com.example.testingProject.dto.request.CreateUserAnswerDto;
import com.example.testingProject.repository.AnswerRepository;
import com.example.testingProject.repository.QuestionRepository;
import com.example.testingProject.repository.UserAnswerRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAnswerControllerTest extends BaseSpringTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserAnswerRepository userAnswerRepository;

    @Autowired
    protected AuthService authService;

    private Long questionId;
    private Long userId;


    @BeforeEach
    public void init() {
        userId = userRepository.save(User.builder()
                .login("login")
                .password(passwordEncoder.encode("login"))
                .userRoles(UserRoles.USER)
                .build());

        questionId = questionRepository.save(Question.builder()
                        .question("question")
                        .questionType(QuestionType.FREECHOICE)
                .build());

        var user = new User();
        user.setId(userId);

        var question = new Question();
        question.setId(questionId);

        answerRepository.save(new Answer("answer", question, user, true));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldCreateUserAnswer() {

        var request = CreateUserAnswerDto.builder()
                .questionId(questionId)
                .rightAnswerFourChoiceId(null)
                .rightAnswerFreeChoice("answer")
                .build();

        mockMvc.perform(post("/user_answer/create")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authService.auth(AuthorizationRequest.builder()
                        .login("login")
                        .password("login")
                        .build()).getToken()))
                .andExpect(status().isNoContent())
                .andReturn();

        var userAnswer = userAnswerRepository.findById(1L);

        assertNotNull(userAnswer, "question not created");
        assertEquals(userAnswer.getUser().getId(), userId);
        assertEquals(userAnswer.getAnswerFreeChoice(), request.getRightAnswerFreeChoice());
        assertNull(userAnswer.getAnswerFourChoice());
        assertEquals(userAnswer.getQuestion().getId(), request.getQuestionId());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shouldGetMyAnswer() {

        var request = CreateUserAnswerDto.builder()
                .questionId(questionId)
                .rightAnswerFourChoiceId(null)
                .rightAnswerFreeChoice("answer")
                .build();

        mockMvc.perform(post("/user_answer/create")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authService.auth(AuthorizationRequest.builder()
                                .login("login")
                                .password("login")
                                .build()).getToken()))
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/user_answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authService.auth(AuthorizationRequest.builder()
                                .login("login")
                                .password("login")
                                .build()).getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("question"))
                .andExpect(jsonPath("$[0].answer").value(request.getRightAnswerFreeChoice()))
                .andReturn();
    }
}
