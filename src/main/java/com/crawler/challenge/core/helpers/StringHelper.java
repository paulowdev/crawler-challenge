package com.crawler.challenge.core.helpers;

import java.security.SecureRandom;

public class StringHelper {

    private static final String ALPHA_NUM_REGEX = "^[a-zA-Z0-9]*$";
    private static final String ALPHA_NUM_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom SEC_RANDOM = new SecureRandom();

    public static String genRandomAlphaNum(final int strLength) {
        var sb = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            sb.append(ALPHA_NUM_CHAR.charAt(SEC_RANDOM.nextInt(ALPHA_NUM_CHAR.length())));
        }
        return sb.toString();
    }

    public static boolean isAlphaNumeric(final String alphaNumeric) {
        return alphaNumeric.matches(ALPHA_NUM_REGEX);
    }

}
