package com.aurora.backend.exception;

public class IllegalCommandParamException extends IllegalArgumentException {

    public IllegalCommandParamException(final String message) {
        super(message);
    }
}
