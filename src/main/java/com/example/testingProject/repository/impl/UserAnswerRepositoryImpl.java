package com.example.testingProject.repository.impl;

import com.example.testingProject.domain.Answer;
import com.example.testingProject.domain.Question;
import com.example.testingProject.domain.User;
import com.example.testingProject.domain.UserAnswer;
import com.example.testingProject.repository.UserAnswerRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserAnswerRepositoryImpl implements UserAnswerRepository {

    private static final String CREATE_USER_ANSWER = """
            insert into user_answer
            (created_at, answer, question_id, user_id, answer_four_choice_id) values
            (:created_at, :answer, :question_id, :user_id, :answer_four_choice_id)
            """;

    private static final String FIND_BY_ID = """
            select
                user_answer.*, questions.question
            from
                user_answer
            join questions on questions.id = user_answer.question_id
            where
                user_answer.id = :id
            """;

    private static final String FIND_MY_ANSWER = """
            select user_answer.*, questions.question from user_answer
            join questions on questions.id = user_answer.question_id
            where user_id = :user_id
            """;

    private static final String FIND_ALL_USER_START_TESTING = """
            select count(user_id) from
            (select user_id, count(user_id) as myCount from user_answer
            group by user_id
            ) as r
            """;

    private static final String FIND_ALL_USER_PASS_TESTING = """
            select count(user_id) from
            (select user_id, count(user_id) as myCount from user_answer
            group by user_id
            having count(user_id) = 5) as r
            """;

    private static final String FIND_ALL_USER_RIGHT_PASS_TESTING = """
            select count(user_id) from
            (select user_id, count(user_id) as myCount from user_answer
             where is_right = true
            group by user_id
            having count(user_id) = 5) as r
            """;

    private static final String FIND_COUNT_RIGHT_ANSWER = """
            select count(*) from user_answer
             where user_id = :user_id and is_right = true
            """;

    private static final String FIND_COUNT_USERS_BETTER_ME = """
            select count(user_id) from
            (select user_id, count(user_id) from user_answer
             where is_right = true and user_id != :user_id
            group by user_id
            having count(user_id) > :my_count) as r
            """;

    private static final String FIND_COUNT_USERS_WORSE_ME = """
            select count(user_id) from
            (select user_id, count(user_id) from user_answer
             where is_right = true and user_id != :user_id
            group by user_id
            having count(user_id) < :my_count) as r
            """;

    private static final String FIND_LIST_QUESTION_ID = """
            select question_id from user_answer
                        where user_id = :user_id
            """;

    private static final RowMapper<UserAnswer> USER_ANSWER_ROW_MAPPER = (rs, rowNum) -> {
        UserAnswer answer = new UserAnswer();
        answer.setId(rs.getLong("id"));
        answer.setAnswerFreeChoice(rs.getString("answer"));
        answer.setRight(rs.getBoolean("is_right"));
        answer.setQuestion(toQuestion(rs));
        answer.setUser(toUser(rs));
        answer.setAnswerFourChoice(toAnswer(rs));
        answer.setCreatedAt(rs.getTimestamp("created_at"));
        answer.setUpdatedAt(rs.getTimestamp("updated_at"));
        answer.setDeletedAt(rs.getTimestamp("deleted_at"));
        return answer;
    };

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UserAnswer findById(Long id) {
        List<UserAnswer> answers = namedParameterJdbcTemplate.query(FIND_BY_ID, Map.of(
                "id", id
        ), USER_ANSWER_ROW_MAPPER);

        if (answers.isEmpty()) {
            return null;
        }

        return answers.get(0);
    }

    @Override
    public Long save(UserAnswer answer) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("answer", answer.getAnswerFreeChoice());
        params.addValue("is_right", answer.isRight());
        params.addValue("created_at", Objects.nonNull(answer.getCreatedAt()) ?
                answer.getCreatedAt() : new Date());
        params.addValue("question_id", Objects.nonNull(answer.getQuestion()) ?
                answer.getQuestion().getId() : null);
        params.addValue("user_id", Objects.nonNull(answer.getUser()) ?
                answer.getUser().getId() : null);
        params.addValue("answer_four_choice_id", Objects.nonNull(answer.getAnswerFourChoice()) ?
                answer.getAnswerFourChoice().getId() : null);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = namedParameterJdbcTemplate.update(CREATE_USER_ANSWER, params, keyHolder, new String[]{"id"});

        if (rows != 1) {
            throw new IllegalStateException("Failed to save user_answer with id " + answer.getId());
        }

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<UserAnswer> getMyAnswer(Long id) {
        List<UserAnswer> answers = namedParameterJdbcTemplate.query(
                FIND_MY_ANSWER,
                Map.of(
                "user_id", id
                ),
                USER_ANSWER_ROW_MAPPER
        );

        if (answers.isEmpty()) {
            return List.of();
        }

        return answers;
    }

    @Override
    public Long getCountUsersStartTesting() {
        return namedParameterJdbcTemplate.query(FIND_ALL_USER_START_TESTING, rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public Long getCountUsersPassTesting() {
        return namedParameterJdbcTemplate.query(FIND_ALL_USER_PASS_TESTING, rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public Long getCountUsersRightPassTesting() {
        return namedParameterJdbcTemplate.query(FIND_ALL_USER_RIGHT_PASS_TESTING, rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public Long getCountRightAnswerByUserId(Long id) {
        return namedParameterJdbcTemplate.query(FIND_COUNT_RIGHT_ANSWER, Map.of(
                "user_id", id), rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public Long getCountUsersBetterMe(Long id, Long myCount) {
        return namedParameterJdbcTemplate.query(FIND_COUNT_USERS_BETTER_ME, Map.of(
                "user_id", id, "my_count", myCount), rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public Long getCountUsersWorseMe(Long id, Long myCount) {
        return namedParameterJdbcTemplate.query(FIND_COUNT_USERS_WORSE_ME, Map.of(
                "user_id", id, "my_count", myCount), rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user_answer count, no data returned");
        });
    }

    @Override
    public List<Long> getQuestionsIds(Long userId) {
        List<Long> questions = namedParameterJdbcTemplate.queryForList(FIND_LIST_QUESTION_ID,Map.of(
                "user_id", userId), Long.class);

        if (questions.isEmpty()) {
            return List.of();
        }

        return questions;
    }

    private static Question toQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("question_id"));
        question.setQuestion(rs.getString("question"));
        if(question.getId() == 0)
            return null;

        return question;
    }

    private static Answer toAnswer(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("answer_four_choice_id"));
        if(answer.getId() == 0)
            return null;

        return answer;
    }

    private static User toUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        if(user.getId() == 0)
            return null;

        return user;
    }
}
