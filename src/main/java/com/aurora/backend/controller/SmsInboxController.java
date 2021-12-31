package com.aurora.backend.controller;

import com.aurora.backend.entity.SmsInbox;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.SmsInboxService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/sms/inbox")
public class SmsInboxController extends BaseController<SmsInbox, Long> {

    @Autowired
    private SmsInboxService smsInboxService;

    @Override
    protected BaseEntityService<SmsInbox, Long> getService() {
        return smsInboxService;
    }
}
