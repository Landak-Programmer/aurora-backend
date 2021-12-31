package com.aurora.backend.services;

import com.aurora.backend.entity.SmsInbox;
import com.aurora.backend.entity.User;
import com.aurora.backend.entity.Wallet;
import com.aurora.backend.exception.AmountNotEnoughException;
import com.aurora.backend.exception.SmartContextException;
import com.aurora.backend.repo.SmsInboxRepository;
import com.aurora.backend.security.UserAware;
import com.aurora.backend.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

@Service
public class SmsInboxService extends BaseEntityService<SmsInbox, Long> {

    @Autowired
    private SmsInboxRepository smsInboxRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

    @Override
    protected PagingAndSortingRepository<SmsInbox, Long> getRepository() {
        return smsInboxRepository;
    }

    @Override
    protected void preCreate(SmsInbox entity) {
        if (entity.getUser() == null) {
            final User user = userService.getById((Long) getAuthenticatedUser().getId());
            entity.setUser(user);
        }

        try {
            final String hash = SHA256.hash(entity.getMessage());
            entity.setHash(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(String.format("Unable to encode sms message %s", entity.getMessage()));
        }

    }

    @Override
    public SmsInbox update(final Long id, final SmsInbox changeSet) {
        throw new UnsupportedOperationException("SmsInbox cannot be updated!");
    }

    public void smsSmartContext(final String message, final UserAware userAware) throws AmountNotEnoughException {
        final String localMessage = normalize(message);
        if (isBankTransaction(localMessage)) {
            final int sendScore = calculateSendScore(localMessage);
            final int receiveScore = calculateReceiveScore(localMessage);
            final Wallet wallet = walletService.walletSmartContext(localMessage, userAware);
            final BigDecimal smartAmount = this.deduceAmount(localMessage);

            if (receiveScore > sendScore) {
                walletService.add(wallet.getId(), smartAmount, "SmartContext sms add transaction");
            } else {
                walletService.minus(wallet.getId(), smartAmount, "SmartContext sms minus transaction");
            }
        }
    }

    private boolean isBankTransaction(final String message) {
        return calculateScore(message) >= 50;
    }

    private String normalize(final String message) {
        return message.replaceAll("RM0", "")
                .replaceAll("RM 0", "");
    }

    /**
     * Max score 100
     *
     * @param message
     * @return
     */
    private int calculateScore(final String message) {
        int score = 0;
        if (message.contains("BIMB"))
            score += 80;
        if (message.contains("RM"))
            score += 20;
        return score;
    }

    /**
     * Max receive score 100
     *
     * @param message
     * @return
     */
    private int calculateReceiveScore(final String message) {
        int score = 0;
        if (message.contains("receive"))
            score += 100;
        return score;
    }

    /**
     * Max receive score 100
     *
     * @param message
     * @return
     */
    private int calculateSendScore(final String message) {
        int score = 0;
        if (message.contains("ACCEPTED"))
            score += 60;
        if (message.contains("SUCCESS"))
            score += 40;
        return score;
    }

    // todo: ugly....
    private BigDecimal deduceAmount(final String message) {
        boolean detect = false;
        boolean partialDetect = false;
        StringBuilder construct = new StringBuilder();

        for (char c : message.toCharArray()) {

            if (detect) {
                if (c == ' ') {
                    continue;
                }

                if (Character.isDigit(c) || c == '.') {
                    construct.append(c);
                } else if (construct.length() > 0) {
                    return new BigDecimal(construct.toString().trim());
                } else {
                    detect = false;
                    partialDetect = false;
                }
            }

            if (partialDetect) {
                if (c == 'M' || c == 'm') {
                    detect = true;
                    continue;
                }
                partialDetect = false;
            }

            if (c == 'R' || c == 'r') {
                partialDetect = true;
            }
        }
        throw new SmartContextException(String.format("Unable to deduce amount based on message %s", message));
    }
}
