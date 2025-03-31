package com.exam.phonenumberservice.exception;

public class PhoneNumberAlreadyActivatedException extends RuntimeException {
    public PhoneNumberAlreadyActivatedException(String message) {
        super(message);
    }
}
