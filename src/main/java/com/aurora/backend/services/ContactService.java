package com.aurora.backend.services;

import com.aurora.backend.entity.Contact;
import com.aurora.backend.entity.User;
import com.aurora.backend.repo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Contact getContactByName(final String name) {
        return contactRepository.findByName(name);
    }

    public List<Contact> getContactByNameStartWith(final String name) {
        return contactRepository.findByNameStartsWith(name);
    }

    public List<Contact> getContactByNameEndsWith(final String name) {
        return contactRepository.findByNameEndsWith(name);
    }

    public List<Contact> getContactByNameContaining(final String name) {
        return contactRepository.findByNameContaining(name);
    }


}
