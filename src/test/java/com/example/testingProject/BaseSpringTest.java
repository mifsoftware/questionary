package com.example.testingProject;

import com.example.testingProject.fixtures.initializers.PostgresInitializer;
import com.example.testingProject.service.AuthService;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {
        PostgresInitializer.class,
        BaseSpringTest.class
})
@AutoConfigureMockMvc
public class BaseSpringTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

    }

    @Autowired
    protected AuthService authService;
    @Autowired
    public PasswordEncoder passwordEncoder;

}
