package com.aurora.backend.entity.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityField {

    boolean isId() default false;

    boolean canGenericUpdate() default true;

    boolean isForeignKey() default false;
}
