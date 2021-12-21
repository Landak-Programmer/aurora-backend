package com.aurora.backend;

import com.aurora.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ContextStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
    }

}
