package com.example.testingProject.repository.impl;

import com.example.testingProject.domain.*;
import com.example.testingProject.domain.types.QuestionType;
import com.example.testingProject.repository.QuestionRepository;
import com.example.testingProject.util.EnumTools;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ArrayUtils;
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
public class QuestionRepositoryImpl implements QuestionRepository {

    private static final String CREATE_QUESTION = """
            insert into questions
            (created_at, question, question_type, created_by_user_id) values
            (:created_at, :question, :question_type, :created_by_user_id)
            """;

    private static final String FIND_BY_ID = """
            select
                *
            from
                questions
            where
                id = :id
            """;

    private static final String FIND_BY_ALL = """
            select
                *
            from
                questions
            where
                COALESCE(array_length(:ids, 1), 0) = 0 or id != any(:ids)
            """;

    private static final String GET_COUNT = """
            select 
                count(*) 
            from 
                questions
            """;

    private static final RowMapper<Question> QUESTION_ROW_MAPPER = (rs, rowNum) -> {
        Question question = new Question();
        question.setId(rs.getLong("id"));
        question.setQuestion(rs.getString("question"));
        question.setCreatedByUser(toUser(rs));
        question.setQuestionType(EnumTools.valueOf(rs.getString("question_type"), QuestionType.class));
        question.setCreatedAt(rs.getTimestamp("created_at"));
        question.setUpdatedAt(rs.getTimestamp("updated_at"));
        question.setDeletedAt(rs.getTimestamp("deleted_at"));
        return question;
    };

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Question> findById(Long id) {
        List<Question> questions = namedParameterJdbcTemplate.query(FIND_BY_ID, Map.of(
                "id", id
        ), QUESTION_ROW_MAPPER);

        if (questions.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(questions.get(0));
    }

    @Override
    public Long save(Question question) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("question", question.getQuestion());
        params.addValue("question_type", question.getQuestionType().toString());
        params.addValue("created_at", Objects.nonNull(question.getCreatedAt()) ?
                question.getCreatedAt() : new Date());
        params.addValue("created_by_user_id", Objects.nonNull(question.getCreatedByUser()) ?
                question.getCreatedByUser().getId() : null);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = namedParameterJdbcTemplate.update(CREATE_QUESTION, params, keyHolder, new String[]{"id"});

        if (rows != 1) {
            throw new IllegalStateException("Failed to save question with id " + question.getId());
        }

        return keyHolder.getKey().longValue();
    }

    @Override
    public Long count() {
        return namedParameterJdbcTemplate.query(GET_COUNT, rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query question count, no data returned");
        });
    }

    @Override
    public List<Question> getAll(List<Long> ids) {
        List<Question> questions = namedParameterJdbcTemplate.query(FIND_BY_ALL,Map.of(
                "ids", toArray(ids)), QUESTION_ROW_MAPPER);

        if (questions.isEmpty()) {
            return List.of();
        }

        return questions;
    }

    private static Answer toAnswer(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("right_answer_id"));
        if(answer.getId() == 0)
            return null;

        return answer;
    }

    private static User toUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("created_by_user_id"));
        if(user.getId() == 0)
            return null;

        return user;
    }

    public long[] toArray(List<Long> items) {
        return ArrayUtils.toPrimitive(items.toArray(new Long[0]));
    }

}
