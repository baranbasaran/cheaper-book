package com.baranbasaran.cheaperbook.exception;

import com.baranbasaran.cheaperbook.common.exceptionhandling.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long id) {
        super("USER_NOT_FOUND", "User with id %d not found.".formatted(id), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User not found.", HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super("USER_NOT_FOUND", "User with email %s not found.".formatted(email), HttpStatus.NOT_FOUND);
    }
}
