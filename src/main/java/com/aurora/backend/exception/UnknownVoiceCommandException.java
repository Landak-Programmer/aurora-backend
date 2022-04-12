package com.aurora.backend.exception;

public class UnknownVoiceCommandException extends RuntimeException {

    public UnknownVoiceCommandException(final String message) {
        super(message);
    }
}
