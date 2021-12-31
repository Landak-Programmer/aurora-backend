package com.aurora.backend.dto;

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
}
