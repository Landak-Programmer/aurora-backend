package com.aurora.backend.entity;

import java.time.LocalDateTime;

public interface HasDateUpdated {

    LocalDateTime getLastUpdated();

    void setLastUpdated(final LocalDateTime lastUpdated);

}
