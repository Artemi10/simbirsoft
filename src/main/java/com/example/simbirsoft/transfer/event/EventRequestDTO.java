package com.example.simbirsoft.transfer.event;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.*;

public record EventRequestDTO(long appId, String name, String extraInformation) {

    public void check() {
        checkName();
        checkExtraInformation();
    }

    private void checkName() {
        if (!isTextFieldValid(name)) {
            throw new ValidatorException("Введено некорректное название события");
        }
    }

    private void checkExtraInformation() {
        if (!isTextFieldValid(extraInformation)) {
            throw new ValidatorException("Поле дополнительная информация не должно быть пустым");
        }
    }
}
