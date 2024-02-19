package com.example.foobook_android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    public static boolean validatePassword(String password) {
        // checks if contains at least 1 letter and 1 number
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(password);

        boolean passwordLength = password.length() >= 8;

        return matcher.find() && passwordLength;
    }
}
