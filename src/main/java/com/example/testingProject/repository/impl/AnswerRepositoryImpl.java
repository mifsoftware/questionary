package com.example.testingProject.repository.impl;

import com.example.testingProject.domain.Answer;
import com.example.testingProject.domain.Question;
import com.example.testingProject.domain.User;
import com.example.testingProject.repository.AnswerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private static final String CREATE_ANSWER = """
            insert into answers
            (created_at, answer, question_id, created_by_user_id, is_right) values
            (:created_at, :answer, :question_id, :created_by_user_id, :is_right)
            """;

    private static final String FIND_BY_ID = """
            select
                *
            from
                answers
            where
                id = :id
            """;

    private static final String FIND_BY_QUESTION_ID = """
            select
                *
            from
                answers
            where
                question_id = :id
            """;

    private static final String FIND_RIGHT_ANSWER = """
            select
                *
            from
                answers
            where
                question_id = :id
                and is_right = true
            """;

    private static final RowMapper<Answer> ANSWER_ROW_MAPPER = (rs, rowNum) -> {
        Answer answer = new Answer();
        answer.setId(rs.getLong("id"));
        answer.setAnswer(rs.getString("answer"));
        answer.setIsRight(rs.getBoolean("is_right"));
        answer.setQuestion(toQuestion(rs));
        answer.setCreatedByUser(toUser(rs));
        answer.setCreatedAt(rs.getTimestamp("created_at"));
        answer.setUpdatedAt(rs.getTimestamp("updated_at"));
        answer.setDeletedAt(rs.getTimestamp("deleted_at"));
        return answer;
    };

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Answer findById(Long id) {
        List<Answer> answers = namedParameterJdbcTemplate.query(FIND_BY_ID, Map.of(
                "id", id
        ), ANSWER_ROW_MAPPER);

        if (answers.isEmpty()) {
            return null;
        }

        return answers.get(0);
    }
    @Override
    public List<Answer> findByQuestionId(Long id) {
        List<Answer> answers = namedParameterJdbcTemplate.query(FIND_BY_QUESTION_ID, Map.of(
                "id", id
        ), ANSWER_ROW_MAPPER);

        if (answers.isEmpty()) {
            return null;
        }

        return answers;
    }
    @Override
    public Optional<Answer> findRightAnswerByQuestionId(Long id) {
        List<Answer> answers = namedParameterJdbcTemplate.query(FIND_RIGHT_ANSWER, Map.of(
                "id", id
        ), ANSWER_ROW_MAPPER);

        if (answers.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(answers.get(0));
    }

    @Override
    public Long save(Answer answer) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("answer", answer.getAnswer());
        params.addValue("is_right", answer.getIsRight());
        params.addValue("created_at", Objects.nonNull(answer.getCreatedAt()) ?
                answer.getCreatedAt() : new Date());
        params.addValue("question_id", Objects.nonNull(answer.getQuestion()) ?
                answer.getQuestion().getId() : null);
        params.addValue("created_by_user_id", Objects.nonNull(answer.getCreatedByUser()) ?
                answer.getCreatedByUser().getId() : null);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = namedParameterJdbcTemplate.update(CREATE_ANSWER, params, keyHolder, new String[]{"id"});

        if (rows != 1) {
            throw new IllegalStateException("Failed to save answer with id " + answer.getId());
        }

        return keyHolder.getKey().longValue();
    }

    private static Question toQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("question_id"));
        if(question.getId() == 0)
            return null;

        return question;
    }

    private static User toUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("created_by_user_id"));
        if(user.getId() == 0)
            return null;

        return user;
    }
}
