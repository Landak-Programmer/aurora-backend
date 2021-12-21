package com.aurora.backend.controller;

import com.aurora.backend.entity.Wallet;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.WalletService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/wallet")
public class WalletController extends BaseController<Wallet, Long> {

    @Autowired
    private WalletService walletService;

    @Override
    protected BaseEntityService<Wallet, Long> getService() {
        return walletService;
    }
}
