package com.example.testingProject.configuration;

import com.example.testingProject.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfigurations extends WebSecurityConfigurerAdapter {

    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public WebSecurityConfigurations(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/auth", "/auth/register")
                .antMatchers(
                        "/v3/api-docs/**", "/swagger-ui/**"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .sessionManagement()
                .disable()
                .authorizeRequests()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}