package com.example.testingProject.configuration;

import com.example.testingProject.service.impl.UserToTokenDetailsMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MapperConfiguration {

    @Bean
    public UserToTokenDetailsMapper userToTokenDetailsMapper() {
        return new UserToTokenDetailsMapper();
    }

}
