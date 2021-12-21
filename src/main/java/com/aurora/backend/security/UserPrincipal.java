package com.aurora.backend.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

    private UserAware user;

    public UserPrincipal(UserAware user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getCredUsername(), "", authorities);
        this.user = user;
    }

    public UserAware getUser() {
        return this.user;
    }

}
