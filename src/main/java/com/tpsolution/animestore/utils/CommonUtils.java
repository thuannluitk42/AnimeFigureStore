package com.tpsolution.animestore.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String regex = "[^a-zA-Z0-9\\sáàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬĐÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ]";

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

    public static String randomIdentifier() {
        Set<String> identifiers = new HashSet<String>();
        String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        Random rand = new java.util.Random();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().isEmpty()) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public static boolean containsSpecialCharacter(String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        // check if contains special character return true
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isImageFile(String fileName) {
        if (fileName == null)
            return false;

        String extension = getFileExtension(fileName);

        if (extension != null) {
            String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};
            for (String ext : imageExtensions) {
                if (extension.equalsIgnoreCase(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
