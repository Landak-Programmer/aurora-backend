package com.aurora.backend.services;

import com.aurora.backend.entity.Transaction;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.SortedSet;

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
    protected void preCreate(final Transaction entity) {
        final Wallet wallet = walletService.getById(entity.getWallet().getId());
        entity.setAfterAmount(wallet.getAmount().add(entity.getAmount()));

        if (entity.getType() == null) {
            entity.setType(Transaction.Type.SPENDING);
        }
    }

    @Override
    public Transaction update(final Long id, final Transaction changeSet) {
        throw new UnsupportedOperationException("Transaction cannot be updated!");
    }

    public SortedSet<String> getTypesOfTransaction() {
        return Transaction.Type.getTypesOfTransaction();
    }
}
