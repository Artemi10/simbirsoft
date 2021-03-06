package com.example.simbirsoft.transfer;

public class Validator {

    public static boolean isTextFieldValid(String password){
        return password != null && !password.isBlank();
    }

    public static boolean isEmailValid(String email) {
        var regexp = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        return email != null && email.matches(regexp);
    }
}
