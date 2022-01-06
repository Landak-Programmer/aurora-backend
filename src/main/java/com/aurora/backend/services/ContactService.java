package com.aurora.backend.services;

import com.aurora.backend.entity.Contact;
import com.aurora.backend.entity.User;
import com.aurora.backend.repo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService extends BaseEntityService<Contact, Long> {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserService userService;

    @Override
    protected PagingAndSortingRepository<Contact, Long> getRepository() {
        return contactRepository;
    }

    @Override
    protected void preCreate(Contact entity) {
        if (entity.getUser() == null) {
            final User user = userService.getById((Long) getAuthenticatedUser().getId());
            entity.setUser(user);
        }
    }


}
