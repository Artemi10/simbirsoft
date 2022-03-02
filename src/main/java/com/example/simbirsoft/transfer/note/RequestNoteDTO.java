package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.exception.ValidatorException;

import static com.example.simbirsoft.transfer.Validator.isTextFieldValid;

public record RequestNoteDTO(String title, String text) {

    public void check() {
        checkTitle();
        checkText();
    }

    private void checkTitle() {
        if (!isTextFieldValid(title)) {
            throw new ValidatorException("Введён некорректный заголовок");
        }
    }

    private void checkText() {
        if (!isTextFieldValid(text)) {
            throw new ValidatorException("Введён некорректный текст");
        }
    }
}
