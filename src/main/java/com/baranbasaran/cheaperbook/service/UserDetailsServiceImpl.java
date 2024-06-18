package com.baranbasaran.cheaperbook.service;

import com.baranbasaran.cheaperbook.model.User;
import com.baranbasaran.cheaperbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User daoUser = userRepository.findByEmail(email).orElse(null);
        if (daoUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(daoUser.getEmail(), daoUser.getPassword(),
            new ArrayList<>());
    }

}