package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.AuthenticationException;

import static com.example.simbirsoft.transfer.auth.Validator.*;

public record LogInDTO(String email, String password) {
    public void check() {
        checkEmail();
        checkPassword();
    }

    private void checkEmail() {
        if (!isEmailValid(email)) {
            throw new AuthenticationException("Введён некорректный email");
        }
    }

    private void checkPassword() {
        if (!isPasswordValid(password)) {
            throw new AuthenticationException("Введён некорректный пароль");
        }
    }
}
