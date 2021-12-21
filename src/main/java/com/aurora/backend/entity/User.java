package com.aurora.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.aurora.backend.entity.core.EntityField;
import com.aurora.backend.security.UserAware;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends JpaEntity<Long> implements UserAware<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EntityField(isId = true)
    private Long id;

    @NotNull
    @EntityField
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;

    @NotNull
    @EntityField(canGenericUpdate = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @EntityField
    private String name;

    @NotNull
    @EntityField(canGenericUpdate = false)
    @JsonIgnore
    private String token;

    @NotNull
    @EntityField
    private String email;

    @NotNull
    @EntityField
    private String phoneNumber;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private Boolean active;

    @Override
    public String getAuthToken() {
        return token;
    }

    @Override
    public String getCredUsername() {
        if (username != null) {
            return username;
        } else {
            return email;
        }
    }

}
