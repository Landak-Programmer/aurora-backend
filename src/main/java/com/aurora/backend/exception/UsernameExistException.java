package com.aurora.backend.exception;

import javax.persistence.EntityExistsException;

public class UsernameExistException extends EntityExistsException {

    public UsernameExistException(final String msg) {
        super(msg);
    }

}
