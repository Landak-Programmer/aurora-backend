package com.aurora.backend.dto;

import java.math.BigDecimal;

public class WalletOperationDto {

    public BigDecimal amount;
    public String operation;
    public Long addWalletId;
    public Long minusWalletId;
}
