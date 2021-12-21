package com.aurora.backend.exception;

import javax.persistence.EntityExistsException;

public class PhoneExistException extends EntityExistsException {

    public PhoneExistException(final String msg) {
        super(msg);
    }

}
