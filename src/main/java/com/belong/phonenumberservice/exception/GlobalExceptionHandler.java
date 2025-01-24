package com.belong.phonenumberservice.exception;

import com.belong.phonenumberservice.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PhoneNumberNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePhoneNumberNotFound(PhoneNumberNotFoundException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("PHONE_NUMBER_NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerNotFound(CustomerNotFoundException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("CUSTOMER_NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidActivationCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidActivationCode(InvalidActivationCodeException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("INVALID_ACTIVATION_CODE")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PhoneNumberAlreadyActivatedException.class)
    public ResponseEntity<ErrorResponseDto> handlePhoneNumberAlreadyActivated(PhoneNumberAlreadyActivatedException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("PHONE_NUMBER_ALREADY_ACTIVATED")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("VALIDATION_FAILED")
                .message("Invalid request parameters")
                .details(errors)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDto> handleIOException(IOException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}