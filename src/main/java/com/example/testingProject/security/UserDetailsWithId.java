package com.example.testingProject.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsWithId extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public UserDetailsWithId(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
