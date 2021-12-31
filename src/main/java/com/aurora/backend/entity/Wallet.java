package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet extends JpaEntity<Long> {

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
    @EntityField
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @EntityField(canGenericUpdate = false)
    private Type type;

    @NotNull
    @EntityField(canGenericUpdate = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal amount;

    @Override
    public Long getId() {
        return id;
    }

    public enum Type {

        BANK("bank"),
        INVESTMENT("investment");

        final String name;

        Type(final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

    }

    @Getter
    public enum Transaction {

        ADD("add"),
        MINUS("minus"),
        TRANSACTION("transaction");

        private final String name;

        Transaction(final String name) {
            this.name = name;
        }
    }
}
