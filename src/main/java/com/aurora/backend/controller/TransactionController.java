package com.aurora.backend.controller;

import com.aurora.backend.entity.Transaction;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.SortedSet;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController extends BaseController<Transaction, Long> {

    @Autowired
    private TransactionService transactionService;

    @Override
    protected BaseEntityService<Transaction, Long> getService() {
        return transactionService;
    }

    @Operation(summary = "Get all type of transaction",
            description = "Get all type of transaction",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(value = {
            "/types"
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SortedSet<String>> getTypesOfTransaction() {
        return ResponseEntity
                .ok()
                .body(transactionService.getTypesOfTransaction());
    }
}
