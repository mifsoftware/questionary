package com.example.testingProject.fixtures.initializers;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.function.Consumer;

public class PostgresInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("sample_test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(true)
            .withCreateContainerCmdModifier(withMemoryLimit(512));

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        POSTGRE_SQL_CONTAINER.start();
        TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRE_SQL_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRE_SQL_CONTAINER.getPassword(),
                "spring.datasource.hikari.minimum-idle=5"
        ).applyTo(configurableApplicationContext.getEnvironment());
    }

    private static Consumer<CreateContainerCmd> withMemoryLimit(int megabytes) {
        return cmd -> {
            if (cmd.getHostConfig() != null) {
                cmd.getHostConfig().withMemory(megabytes * 1024 * 1024L);
            }
        };
    }
}