package com.example.testingProject.repository.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.domain.types.UserRoles;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.util.EnumTools;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String CREATE_USER = """
            insert into users
            (created_at, login, password, user_role) values
            (:created_at, :login, :password, :user_role)
            """;

    private static final String FIND_BY_LOGIN = """
            select
                *
            from
                users
            where
                (LOWER(login) LIKE LOWER(:login))
            """;

    private static final String FIND_BY_ID = """
            select
                *
            from
                users
            where
                id = :id
            """;

    private static final String GET_COUNT = """
            select count(id) from users
            """;

    private static final String FIND_BY_LOGIN_AND_PASSWORD = """
            select
                *
            from
                users
            where
                (LOWER(login) LIKE LOWER(:login))
            and
                (LOWER(password) LIKE LOWER(:password))
            """;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setLogin(rs.getString("login"));
        user.setId(rs.getLong("id"));
        user.setPassword(rs.getString("password"));
        user.setUserRoles(EnumTools.valueOf(rs.getString("user_role"), UserRoles.class));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setDeletedAt(rs.getTimestamp("deleted_at"));
        return user;
    };

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<User> findByLogin(String login) {
        List<User> user = namedParameterJdbcTemplate.query(FIND_BY_LOGIN, Map.of(
                "login", login
        ), USER_ROW_MAPPER);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(user.get(0));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        List<User> user = namedParameterJdbcTemplate.query(FIND_BY_LOGIN, Map.of(
                "login", login, "password", password
        ), USER_ROW_MAPPER);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(user.get(0));
    }


    @Override
    public User findById(Long id) {
        List<User> user = namedParameterJdbcTemplate.query(FIND_BY_ID, Map.of(
                "id", id
        ), USER_ROW_MAPPER);

        if (user.isEmpty()) {
            return null;
        }

        return user.get(0);
    }

    @Override
    public Long save(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", user.getLogin());
        params.addValue("password", user.getPassword());
        params.addValue("user_role", user.getUserRoles().name());
        params.addValue("created_at", user.getCreatedAt() != null ?
                user.getCreatedAt() : new Date());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = namedParameterJdbcTemplate.update(CREATE_USER, params, keyHolder, new String[]{"id"});

        if (rows != 1) {
            throw new IllegalStateException("Failed to save user with id " + user.getId());
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long count() {
        return namedParameterJdbcTemplate.query(GET_COUNT, rs -> {
            if (rs.next()) {
                return rs.getLong("count");
            }
            throw new IllegalStateException("Failed to query user count, no data returned");
        });
    }
}
