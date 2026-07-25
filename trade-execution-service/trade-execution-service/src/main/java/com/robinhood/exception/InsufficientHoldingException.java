package com.robinhood.exception;

public class InsufficientHoldingException extends RuntimeException {

    public InsufficientHoldingException(String message) {
        super(message);
    }
}