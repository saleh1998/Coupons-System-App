package com.example.databases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidInput {
    public static boolean isValidEmail(String email) {
        String pattern = "^[\\w\\.-]+@[\\w\\.-]+\\.com$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

}
