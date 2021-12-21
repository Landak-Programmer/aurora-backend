package com.aurora.backend.exception;

public class InvalidFieldException extends RuntimeException {

    public InvalidFieldException(final String msg) {
        super(msg);
    }
}
