package com.aurora.backend.services;

import com.aurora.backend.dto.WalletOperationDto;
import com.aurora.backend.entity.Transaction;
import com.aurora.backend.entity.User;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.exception.AmountNotEnoughException;
import com.aurora.backend.exception.SmartContextException;
import com.aurora.backend.repo.WalletRepository;
import com.aurora.backend.security.UserAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService extends BaseEntityService<Wallet, Long> {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @Override
    protected PagingAndSortingRepository<Wallet, Long> getRepository() {
        return walletRepository;
    }

    @Override
    protected void preCreate(final Wallet entity) {
        if (entity.getUser() == null) {
            final User user = userService.getById((Long) getAuthenticatedUser().getId());
            entity.setUser(user);
        }
        entity.setAmount(BigDecimal.ZERO);
    }

    @Transactional
    public void operation(final WalletOperationDto dto) throws AmountNotEnoughException {
        final String operation = dto.operation.toLowerCase();
        if (Wallet.Transaction.ADD.getName().equals(operation)) {
            add(dto.addWalletId, dto.amount, dto.reference, dto.type);
        } else if (Wallet.Transaction.MINUS.getName().equals(operation)) {
            minus(dto.minusWalletId, dto.amount, dto.reference, dto.type);
        } else if (Wallet.Transaction.TRANSACTION.getName().equals(operation)) {
            transaction(dto.addWalletId, dto.minusWalletId, dto.amount, dto.reference, dto.type);
        } else {
            throw new UnsupportedOperationException("Unsupported operation!");
        }
    }

    @Transactional
    public void transaction(
            final Long addId,
            final Long minusId,
            final BigDecimal amount,
            final String reference,
            final Transaction.Type type) throws AmountNotEnoughException {
        minus(minusId, amount, reference, type);
        add(addId, amount, reference, type);
    }

    @Transactional
    public void add(final Long id, final BigDecimal amount, final String reference, final Transaction.Type type) {
        final Wallet wallet = super.getById(id);

        // transaction
        final Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setReference(reference);
        transactionService.create(transaction);

        wallet.setAmount(wallet.getAmount().add(amount));
        walletRepository.save(wallet);
    }

    @Transactional
    public void minus(final Long id, final BigDecimal amount, final String reference, final Transaction.Type type) throws AmountNotEnoughException {
        final Wallet wallet = super.getById(id);
        if (wallet.getAmount().compareTo(amount) < 0) {
            throw new AmountNotEnoughException(String.format("Amount for walletId %s not enough!", id));
        }

        // transaction
        final Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount.multiply(new BigDecimal(-1)));
        transaction.setReference(reference);
        transactionService.create(transaction);

        wallet.setAmount(wallet.getAmount().subtract(amount));
        walletRepository.save(wallet);
    }

    /**
     * fixme: support all user!
     *
     * @param message
     * @return
     */
    public Wallet walletSmartContext(final String message, final UserAware userAware) {
        if (message.contains("BIMB")) {
            return walletRepository.findByUserIdAndName((Long) userAware.getId(), "Bank Islam");
        }
        throw new SmartContextException("Unable to deduce the bank!");
    }

    public List<Wallet> getWalletsByCred() {
       return walletRepository.findByUserId((Long) getAuthenticatedUser().getId());
    }
}
