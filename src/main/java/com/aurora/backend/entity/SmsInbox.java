package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "sms_inbox")
public class SmsInbox extends JpaEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EntityField(isId = true)
    private Long id;

    @NotNull
    @ManyToOne
    @EntityField(isForeignKey = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private String message;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private String hash;

}
