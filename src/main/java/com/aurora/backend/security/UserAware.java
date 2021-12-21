package com.aurora.backend.security;

import java.io.Serializable;
import com.aurora.backend.entity.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface UserAware<E extends Serializable> extends IEntity<E>, AccessTokenAware {

    @JsonIgnore
    String getCredUsername();

}
