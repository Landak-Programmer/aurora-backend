package com.aurora.backend.event;

import com.aurora.backend.entity.IEntity;
import com.aurora.backend.security.UserAware;

public class EntityCreatedEvent<T extends IEntity> extends AbstractEntityEvent<T> {

    public EntityCreatedEvent(final T source, final UserAware caller, final String message) {
        super(source, caller, message);
    }
}
