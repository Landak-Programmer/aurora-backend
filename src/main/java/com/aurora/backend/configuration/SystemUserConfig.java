package com.aurora.backend.configuration;

import com.aurora.backend.entity.SystemUser;
import com.aurora.backend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SystemUserConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUserConfig.class);

    // fixme?
    @PostConstruct
    public void init() {
        User systemUser = new User();
        systemUser.setId(0L);
        systemUser.setToken("admin_systemusertoken");
        systemUser.setUsername("SYSTEM USER");
        systemUser.setActive(true);

        SystemUser.setInstance(systemUser);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("System user {} initialized", systemUser.getUsername());
        }
    }

}
