package com.example.simbirsoft.transfer.auth;

import com.example.simbirsoft.exception.AuthenticationException;

import static com.example.simbirsoft.transfer.auth.Validator.*;

public record SignUpDTO(String email, String password, String rePassword) {
    public void check() {
        checkEmail();
        checkPassword();
        checkRePassword();
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

    private void checkRePassword() {
        var isRePasswordValid = password.equals(rePassword);
        if (!isRePasswordValid) {
            throw new AuthenticationException("Пароли не совпадают");
        }
    }
}
