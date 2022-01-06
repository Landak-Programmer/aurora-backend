package com.aurora.backend.entity;

import com.aurora.backend.entity.core.EntityField;
import com.aurora.backend.utils.StringHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction extends JpaEntity<Long> {

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
    @Enumerated(EnumType.STRING)
    @EntityField(canGenericUpdate = false)
    private Type type;

    @NotNull
    @EntityField(canGenericUpdate = false)
    private BigDecimal amount;

    @NotNull
    @EntityField(canGenericUpdate = false)
    @JsonIgnore
    private BigDecimal afterAmount;

    public enum Type {
        SPENDING("spending"),
        ZAKAT("zakat"),
        TAX("tax"),
        CHARITY("charity");

        final String name;

        Type(final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        public static SortedSet<String> getTypesOfTransaction() {
            final SortedSet<String> set = new TreeSet<String>();
            set.add(StringHelper.capitalize(SPENDING.getName()));
            set.add(StringHelper.capitalize(ZAKAT.getName()));
            set.add(StringHelper.capitalize(TAX.getName()));
            set.add(StringHelper.capitalize(CHARITY.getName()));
            return set;
        }
    }


}
