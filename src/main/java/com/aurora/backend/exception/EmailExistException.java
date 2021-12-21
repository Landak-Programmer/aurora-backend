package com.aurora.backend.exception;

import javax.persistence.EntityExistsException;

public class EmailExistException extends EntityExistsException {

    public EmailExistException(final String msg) {
        super(msg);
    }

}
