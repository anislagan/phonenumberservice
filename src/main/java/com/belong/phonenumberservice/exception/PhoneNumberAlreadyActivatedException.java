package com.belong.phonenumberservice.exception;

public class PhoneNumberAlreadyActivatedException extends RuntimeException {
    public PhoneNumberAlreadyActivatedException(String message) {
        super(message);
    }
}
