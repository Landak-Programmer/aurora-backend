package com.aurora.backend.controller;

import com.aurora.backend.dto.WalletOperationDto;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.exception.AmountNotEnoughException;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.WalletService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @Operation(summary = "Add amount to wallet",
            description = "Add amount to wallet",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(value = {
            "/operation"
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public void operation(@RequestBody WalletOperationDto walletOperationDto) throws AmountNotEnoughException {
        walletService.operation(walletOperationDto);
    }
}
