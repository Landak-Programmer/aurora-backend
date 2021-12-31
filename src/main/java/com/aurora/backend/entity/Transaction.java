package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction extends JpaEntity<Long>{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EntityField(isId = true)
    private Long id;

    @NotNull
    @ManyToOne
    @EntityField(isForeignKey = true)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private String reference;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private BigDecimal amount;

    @NotNull
    @EntityField(canGenericUpdate = false)
    @JsonIgnore
    private BigDecimal afterAmount;


}
