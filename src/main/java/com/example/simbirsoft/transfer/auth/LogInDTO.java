package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.*;

public record LogInDTO(String email, String password) {
    public void check() {
        checkEmail();
        checkPassword();
    }

    private void checkEmail() {
        if (!isEmailValid(email)) {
            throw new ValidatorException("Введён некорректный email");
        }
    }

    private void checkPassword() {
        if (!isTextFieldValid(password)) {
            throw new ValidatorException("Введён некорректный пароль");
        }
    }
}
