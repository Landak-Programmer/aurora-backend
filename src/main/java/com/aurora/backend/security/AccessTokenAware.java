package com.aurora.backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AccessTokenAware {

    @JsonIgnore
    String getAuthToken();

}
