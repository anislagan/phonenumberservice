package com.belong.phonenumberservice.util;

import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhoneNumberUtils {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");
    private static final Pattern ACTIVATION_CODE_PATTERN = Pattern.compile("^[0-9]{6}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isValidActivationCode(String activationCode) {
        return activationCode != null && ACTIVATION_CODE_PATTERN.matcher(activationCode).matches();
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : null;
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, DATE_FORMATTER) : null;
    }
}
