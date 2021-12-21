package com.aurora.backend.services;

import com.aurora.backend.exception.IllegalUserTypeException;
import com.aurora.backend.security.IAuthenticationFacade;
import com.aurora.backend.security.UserAware;
import com.aurora.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

public class BaseService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public UserAware getAuthenticatedUser() throws IllegalUserTypeException {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication != null) {
            Object principalObj = authentication.getPrincipal();

            if (principalObj == null) {
                throw new IllegalStateException("Principal is unexpectedly undefined!");
            }

            if (principalObj instanceof UserPrincipal) {
                return ((UserPrincipal) principalObj).getUser();
            }
        }
        throw new IllegalUserTypeException("User are not recognized!");
    }

}
