package com.baranbasaran.cheaperbook.service;

import com.baranbasaran.cheaperbook.model.User;
import com.baranbasaran.cheaperbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    @CachePut(value = "users", key = "'user_' + #result.email", unless = "#result == null")
    public User getAuthenticatedUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(principal.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void authenticate(String email, String password) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, password));
    }

    public String getBasicAuthenticationToken(String email, String password) {
        return java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }
}
