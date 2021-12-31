package com.aurora.backend.event.processor;

import com.aurora.backend.entity.SmsInbox;
import com.aurora.backend.entity.SystemUser;
import com.aurora.backend.event.EntityCreatedEvent;
import com.aurora.backend.exception.AmountNotEnoughException;
import com.aurora.backend.services.SmsInboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class BaseEventProcessor {

    @Autowired
    private SmsInboxService smsInboxService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateEvent(final EntityCreatedEvent event) throws AmountNotEnoughException {
        SystemUser.getInstance().runAs();
        if (event.getSource() instanceof SmsInbox) {
            final SmsInbox source = (SmsInbox) event.getSource();
            smsInboxService.smsSmartContext(source.getMessage(), event.getCaller());
        }
    }
}
