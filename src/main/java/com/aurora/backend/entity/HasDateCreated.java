package com.aurora.backend.entity;

import java.time.LocalDateTime;

public interface HasDateCreated {

    LocalDateTime getDateCreated();

    void setDateCreated(final LocalDateTime dateCreated);

}
