package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.*;

public record SignUpDTO(String email, String password, String rePassword) {
    public void check() {
        checkEmail();
        checkPassword();
        checkRePassword();
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

    private void checkRePassword() {
        var isRePasswordValid = password.equals(rePassword);
        if (!isRePasswordValid) {
            throw new ValidatorException("Пароли не совпадают");
        }
    }
}
