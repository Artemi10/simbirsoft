package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.isEmailValid;
import static com.example.simbirsoft.transfer.Validator.isTextFieldValid;

public record UpdateDTO(String email, String newPassword, String rePassword, String token) {
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
        if (!isTextFieldValid(newPassword)) {
            throw new ValidatorException("Введён некорректный пароль");
        }
    }

    private void checkRePassword() {
        var isRePasswordValid = newPassword.equals(rePassword);
        if (!isRePasswordValid) {
            throw new ValidatorException("Пароли не совпадают");
        }
    }
}
