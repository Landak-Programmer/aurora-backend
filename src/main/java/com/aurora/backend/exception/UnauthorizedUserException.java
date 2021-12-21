package com.aurora.backend.exception;

public class UnauthorizedUserException extends IllegalAccessException {

    public UnauthorizedUserException(final String msg) {
        super(msg);
    }

}
