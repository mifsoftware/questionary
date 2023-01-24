package com.example.testingProject.service.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.domain.UserAnswer;
import com.example.testingProject.dto.request.CreateUserAnswerDto;
import com.example.testingProject.dto.response.UserAnswerDto;
import com.example.testingProject.exception.BaseRuntimeException;
import com.example.testingProject.repository.AnswerRepository;
import com.example.testingProject.repository.QuestionRepository;
import com.example.testingProject.repository.UserAnswerRepository;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.security.UserDetailsWithId;
import com.example.testingProject.service.UserAnswerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserAnswerServiceImpl implements UserAnswerService {

    UserAnswerRepository userAnswerRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    @Override
    public List<UserAnswerDto> getMyAnswer(User user) {
        return userAnswerRepository.getMyAnswer(user.getId()).stream()
                .map(x -> new UserAnswerDto(
                                x.getQuestion().getQuestion(),
                                x.getAnswerFreeChoice()
                        )
                ).toList();
    }

    @Override
    public void create(CreateUserAnswerDto request, User user) {
        var question = questionRepository.findById(request.getQuestionId()).orElseThrow(() -> {
            log.warn("question not found request = {}", request);
            return new BaseRuntimeException("Question not found.");
        });
        var rightAnswer = answerRepository.findRightAnswerByQuestionId(request.getQuestionId()).orElseThrow(() -> {
            log.warn("answer not found request = {}", request);
            return new BaseRuntimeException("Correct answer not found, it has not yet been added.");
        });


        var isRight = false;
        switch (question.getQuestionType()) {
            case FOURNSWERS -> {
                if (request.getRightAnswerFourChoiceId() == null) {
                    log.warn("not setted rightAnswerFourChoiceId field, request = {}", request);
                    throw new BaseRuntimeException("The rightAnswerFourChoiceId field must be filled.");
                }
                isRight = Objects.equals(request.getRightAnswerFourChoiceId(), rightAnswer.getId());
                userAnswerRepository.save(new UserAnswer(question, user, null, rightAnswer, isRight));
            }
            case FREECHOICE -> {
                if (request.getRightAnswerFreeChoice() == null || request.getRightAnswerFreeChoice().isEmpty()) {
                    log.warn("not setted getRightAnswerFreeChoice field, request = {}", request);
                    throw new BaseRuntimeException("The getRightAnswerFreeChoice field must be filled.");
                }

                isRight = Objects.equals(request.getRightAnswerFreeChoice(), rightAnswer.getAnswer());
                userAnswerRepository.save(new UserAnswer(question, user, request.getRightAnswerFreeChoice(), null, isRight));
            }
        }
    }
}
