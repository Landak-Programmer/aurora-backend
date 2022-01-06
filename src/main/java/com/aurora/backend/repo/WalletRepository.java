package com.aurora.backend.repo;

import com.aurora.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByUserIdAndName(final Long userId, final String name);

    List<Wallet> findByUserId(final Long userId);
}
