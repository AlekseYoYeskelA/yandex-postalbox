package com.example.yandex_interview.postalbox.exception;

public class InvalidAccessCodeException extends RuntimeException {
    public InvalidAccessCodeException(String message) {
        super(message);
    }
}
