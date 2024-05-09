package com.tpsolution.animestore.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean checkEmail(String email){
        return patternMatches(email, "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean checkPhoneNumberVietNam(String phoneNumber){
        return patternMatches(phoneNumber, "(84|0[3|5|7|8|9])([0-9]{8})\\b");
    }

    public static boolean checkdayOfBirth(String dayOfBirth){
        return patternMatches(dayOfBirth, "^(0[1-9]|[1-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/\\d{4}$");
    }

    public static String extractUsernameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("^([^@]*)");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Invalid email format";
        }
    }
}
