package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import com.aurora.backend.exception.PreUpdateException;
import com.aurora.backend.utils.StringHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class JpaEntity<ID extends Serializable> implements IEntity<ID>, HasDateUpdated, HasDateCreated {

    @EntityField
    @JsonIgnore
    private LocalDateTime dateCreated = LocalDateTime.now();

    @EntityField
    @JsonIgnore
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // todo: generic them???
    public Object getField(final String fieldName) {
        try {
            final String raadFieldMethodName = getReadFieldMethodName(fieldName);
            final Method method = this
                    .getClass()
                    .getMethod(raadFieldMethodName);
            return method.invoke(this);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new PreUpdateException(e.getMessage());
        }
    }

    public void setField(final String fieldName, final Object value, final Class<?> classType) {
        try {
            final String writeFieldMethodName = getWriteFieldMethodName(fieldName);
            final Method method = this
                    .getClass()
                    .getMethod(writeFieldMethodName, classType);
            method.invoke(this, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new PreUpdateException(e.getMessage());
        }
    }

    public static String getWriteFieldMethodName(final String fieldName) {
        return String.format("set%s", StringHelper.capitalize(fieldName));
    }

    public static String getReadFieldMethodName(final String fieldName) {
        return String.format("get%s", StringHelper.capitalize(fieldName));
    }

}
