package com.aurora.backend.services;

import com.aurora.backend.entity.User;
import com.aurora.backend.exception.EmailExistException;
import com.aurora.backend.exception.PhoneExistException;
import com.aurora.backend.exception.UsernameExistException;
import com.aurora.backend.repo.UserRepository;
import com.aurora.backend.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class UserService extends BaseEntityService<User, Long> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected PagingAndSortingRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected void preCreate(final User entity) {

        usernameExist(entity.getUsername());
        emailExist(entity.getEmail());
        phoneExist(entity.getPhoneNumber());

        try {
            entity.setPassword(SHA256.hash(entity.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode password for user");
        }

        try {
            entity.setToken("USER_" + SHA256.hash(randomAlphanumeric(32)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token for user");
        }

        entity.setActive(true);
    }

    @Override
    protected User preUpdate(final User pEntity, final User entity) {

        if (isChanged(pEntity.getUsername(), entity.getUsername())) {
            usernameExist(entity.getUsername());
        }

        if (isChanged(pEntity.getEmail(), entity.getEmail())) {
            emailExist(entity.getEmail());
        }

        if (isChanged(pEntity.getPhoneNumber(), entity.getPhoneNumber())) {
            failIfAlphabet(entity.getPhoneNumber(), "phone number");
            phoneExist(entity.getPhoneNumber());
        }

        return super.preUpdate(pEntity, entity);
    }

    private void usernameExist(final String username) {
        final User pUser = userRepository.findByUsername(username);
        if (pUser != null) {
            throw new UsernameExistException("Username already exist!");
        }
    }

    private void emailExist(final String email) {
        final User pUser = userRepository.findByEmail(email);
        if (pUser != null) {
            throw new EmailExistException("Email already exist!");
        }
    }

    private void phoneExist(final String phoneNumber) {
        final User pUser = userRepository.findByPhoneNumber(phoneNumber);
        if (pUser != null) {
            throw new PhoneExistException("Phone already exist!");
        }
    }

}
