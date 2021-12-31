package com.aurora.backend.entity;

import com.aurora.backend.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;

public final class SystemUser extends User {

    public static SystemUser systemUser = null;

    private SystemUser() {
    }

    public static SystemUser getInstance() {
        if (systemUser == null) {
            throw new RuntimeException("System user not initialized");
        }
        return systemUser;
    }

    public static void setInstance(final User systemUSer) {
        systemUser = new SystemUser();
        systemUser.setId(systemUSer.getId());
        systemUser.setActive(systemUSer.getActive());
        systemUser.setToken(systemUSer.getAuthToken());
        systemUser.setUsername(systemUSer.getUsername());
    }

    public boolean isSystem() {
        return true;
    }

    public void runAs() {
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new UserPrincipal(this, authorities),
                this.getAuthToken(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
