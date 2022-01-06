package com.aurora.backend.dto;

import com.aurora.backend.entity.Transaction;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WalletOperationDto {

    @NotNull
    public BigDecimal amount;
    @NotNull
    public String operation;
    @NotNull
    public String reference;
    public Long addWalletId;
    public Long minusWalletId;
    public Transaction.Type type;
}
