package com.example.simbirsoft.transfer.application;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.isTextFieldValid;

public record AppRequestDTO(String name) {
    public void check() {
        if (!isTextFieldValid(name)) {
            throw new ValidatorException("Введено некорректное название приложения");
        }
    }
}
