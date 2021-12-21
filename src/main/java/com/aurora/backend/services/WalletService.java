package com.aurora.backend.services;

import com.aurora.backend.dto.WalletOperationDto;
import com.aurora.backend.entity.User;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.exception.AmountNotEnoughException;
import com.aurora.backend.repo.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService extends BaseEntityService<Wallet, Long> {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserService userService;

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
            add(dto.addWalletId, dto.amount);
        } else if (Wallet.Transaction.MINUS.getName().equals(operation)) {
            add(dto.minusWalletId, dto.amount);
        } else if (Wallet.Transaction.TRASACTION.getName().equals(operation)) {
            transaction(dto.addWalletId, dto.minusWalletId, dto.amount);
        } else {
            throw new UnsupportedOperationException("Unsupported operation!");
        }
    }

    @Transactional
    public void transaction(final Long addId, final Long minusId, final BigDecimal amount) throws AmountNotEnoughException {
        minus(minusId, amount);
        add(addId, amount);
    }

    @Transactional
    public void add(final Long id, final BigDecimal amount) {
        final Wallet wallet = super.getById(id);
        wallet.setAmount(wallet.getAmount().add(amount));
        walletRepository.save(wallet);

        // transaction
    }

    @Transactional
    public void minus(final Long id, final BigDecimal amount) throws AmountNotEnoughException {
        final Wallet wallet = super.getById(id);
        if (wallet.getAmount().compareTo(amount) < 0) {
            throw new AmountNotEnoughException(String.format("Amount for walletId %s not enough!", id));
        }
        wallet.setAmount(wallet.getAmount().subtract(amount));
        walletRepository.save(wallet);

        // transaction
    }
}
