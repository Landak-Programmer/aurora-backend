package com.aurora.backend.event;

import com.aurora.backend.security.UserAware;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

public class AbstractEvent extends ApplicationEvent {

    private final UserAware caller;
    private final String message;
    private boolean propagate = true;
    private Map<String, Object> context;

    public AbstractEvent(Object source, UserAware caller, String message) {
        this(source, caller, message, new HashMap<>(), true);
    }

    public AbstractEvent(Object source, UserAware caller, String message, boolean propagate) {
        this(source, caller, message, new HashMap<>(), propagate);
    }

    public AbstractEvent(Object source, UserAware caller, String message, Map<String, Object> context) {
        this(source, caller, message, context, true);
    }

    public AbstractEvent(Object source, UserAware caller, String message, Map<String, Object> context, boolean propagate) {
        super(source);
        this.caller = caller;
        this.message = message;
        this.context = context;
        this.propagate = propagate;
    }

    public UserAware getCaller() {
        return caller;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPropagate() {
        return propagate;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public void addContext(String ctxKey, Object ctxValue) {
        if (context == null) {
            context = new HashMap<>();
        }

        context.put(ctxKey, ctxValue);
    }
}
