package com.aurora.backend.controller;

import com.aurora.backend.entity.Contact;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.ContactService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/contact")
public class ContactController extends BaseController<Contact, Long> {

    @Autowired
    private ContactService contactService;

    @Override
    protected BaseEntityService<Contact, Long> getService() {
        return contactService;
    }
}
