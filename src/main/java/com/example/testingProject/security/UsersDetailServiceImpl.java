package com.example.testingProject.security;

import com.example.testingProject.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersDetailServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";
    private final UserRepository userRepository;

    public UsersDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsWithId(
                user.getId(),
                user.getLogin(),
                "",
                List.of(new SimpleGrantedAuthority(ROLE_PREFIX + user.getUserRoles().name()))
        );
    }
}