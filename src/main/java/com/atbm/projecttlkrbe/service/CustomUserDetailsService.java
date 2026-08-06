package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.repository.AuthRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthRep authRep;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));

        return User.builder()
                .username(auth.getEmail())
                .password(auth.getPassword())
                .roles(auth.getRole().name())
                .build();
    }
}
