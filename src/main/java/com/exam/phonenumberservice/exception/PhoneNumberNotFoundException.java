package com.exam.phonenumberservice.exception;

public class PhoneNumberNotFoundException extends RuntimeException {
    public PhoneNumberNotFoundException(String message) {
        super(message);
    }
}

