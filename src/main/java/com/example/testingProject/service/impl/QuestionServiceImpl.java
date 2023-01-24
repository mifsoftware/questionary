package com.example.testingProject.service.impl;

import com.example.testingProject.domain.Answer;
import com.example.testingProject.domain.Question;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.dto.request.CreateAnswerDto;
import com.example.testingProject.dto.request.CreateQuestionAndAnswerDto;
import com.example.testingProject.dto.response.AnswerDto;
import com.example.testingProject.dto.response.QuestionDto;
import com.example.testingProject.exception.BaseRuntimeException;
import com.example.testingProject.repository.AnswerRepository;
import com.example.testingProject.repository.QuestionRepository;
import com.example.testingProject.repository.UserAnswerRepository;
import com.example.testingProject.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {

    @Value("${questions_max_count}")
    Integer questionsMaxCount;

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    UserAnswerRepository userAnswerRepository;


    @Override
    public void createQuestion(CreateQuestionAndAnswerDto request, User user) {

        var questionCount = questionRepository.count();
        if (questionCount >= questionsMaxCount) {
            log.warn("question count = {}", questionsMaxCount);
            throw new BaseRuntimeException("more than %d questions, you can't add more questions".formatted(questionsMaxCount), HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        if (request.getQuestionType() == QuestionType.FOURNSWERS && request.getAnswer().size() != 4
                || request.getAnswer().stream().filter(CreateAnswerDto::getIsRight).count() > 1)
            throw new BaseRuntimeException("Answers to the question should be 4", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase());

        if (request.getQuestionType() == QuestionType.FREECHOICE && request.getAnswer().size() != 1
                || !request.getAnswer().get(0).getIsRight())
            throw new BaseRuntimeException("Answer should be 1", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase());

        var questionId = questionRepository.save(new Question(request.getQuestion(), request.getQuestionType(), user));

        for (var answer :
                request.getAnswer()) {
            var question = new Question();
            question.setId(questionId);
            answerRepository.save(new Answer(answer.getAnswer(), question, user, answer.getIsRight()));
        }
    }

    @Override
    public QuestionDto getQuestion(Long userId) {
        var questionIds = userAnswerRepository.getQuestionsIds(userId);

        if (questionIds.size() == 5)
            throw new BaseRuntimeException("You have already passed the testng.");

        var question = questionRepository.getAll(questionIds).stream().findFirst()
                .orElseThrow(() -> new BaseRuntimeException("For now, these are all the questions we have."));

        var answers = answerRepository.findByQuestionId(question.getId()).stream().map(x ->
                        AnswerDto.builder()
                                .answer(x.getAnswer())
                                .id(x.getId())
                                .build())
                .toList();

        return QuestionDto.builder()
                .question(question.getQuestion())
                .id(question.getId())
                .questionType(question.getQuestionType())
                .answers(answers)
                .build();
    }
}
