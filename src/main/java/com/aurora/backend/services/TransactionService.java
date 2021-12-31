package com.aurora.backend.services;

import com.aurora.backend.entity.Transaction;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends BaseEntityService<Transaction, Long> {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletService walletService;

    @Override
    protected PagingAndSortingRepository<Transaction, Long> getRepository() {
        return transactionRepository;
    }

    @Override
    protected void preCreate(Transaction entity) {
        final Wallet wallet = walletService.getById(entity.getWallet().getId());
        entity.setAfterAmount(wallet.getAmount().add(entity.getAmount()));
    }

    @Override
    public Transaction update(final Long id, final Transaction changeSet) {
        throw new UnsupportedOperationException("Transaction cannot be updated!");
    }
}
