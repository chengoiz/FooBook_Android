package com.example.foobook_android.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Utility class for password validation.
public class PasswordValidator {

    // Validates the given password.
    public static boolean validatePassword(String password) {
        // Checks if password contains at least 1 letter and 1 number
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(password);

        boolean passwordLength = password.length() >= 8;

        return matcher.find() && passwordLength;
    }
}
