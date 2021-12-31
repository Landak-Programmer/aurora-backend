package com.aurora.backend.controller;

import com.aurora.backend.entity.Transaction;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
