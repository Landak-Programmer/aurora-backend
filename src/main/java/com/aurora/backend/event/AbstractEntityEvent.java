package com.aurora.backend.event;

import com.aurora.backend.entity.IEntity;
import com.aurora.backend.security.UserAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class AbstractEntityEvent<T extends IEntity> extends AbstractEvent implements ResolvableTypeProvider {

    public AbstractEntityEvent(T source, UserAware caller, String message) {
        super(source, caller, message);
    }

    public T getSource() {
        return (T) source;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(source));
    }
}
