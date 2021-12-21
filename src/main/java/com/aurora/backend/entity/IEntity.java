package com.aurora.backend.entity;

import java.io.Serializable;

public interface IEntity<ID extends Serializable> extends Serializable {

    void setId(final ID id);

    ID getId();

}
