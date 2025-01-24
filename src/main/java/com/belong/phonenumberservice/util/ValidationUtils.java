package com.belong.phonenumberservice.util;

public class ValidationUtils {

    public static void validatePageParameters(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page number cannot be less than 1");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
}
