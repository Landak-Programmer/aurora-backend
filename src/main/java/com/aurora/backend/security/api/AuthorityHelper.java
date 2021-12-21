package com.aurora.backend.security.api;

import com.aurora.backend.security.UserAware;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class AuthorityHelper {
    public static Collection<? extends GrantedAuthority> getAuthorities(final UserAware user) {
        return new ArrayList<>();
    }
}
