package com.aurora.backend.services;

import com.aurora.backend.entity.User;
import com.aurora.backend.exception.UnauthorizedUserException;
import com.aurora.backend.repo.UserRepository;
import com.aurora.backend.security.UserAware;
import com.aurora.backend.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserAware login(final String apiKey) {
        return userRepository.findByTokenAndActive(apiKey, true);
    }

    @Transactional(readOnly = true)
    public UserAware<Long> login(final String username, final String password) throws NoSuchAlgorithmException, UnauthorizedUserException {
        final String sha256password = SHA256.hash(password);
        final User user = userRepository.findByUsernameAndPasswordAndActive(username, sha256password, true);
        if (user != null) {
            return user;
        }
        throw new UnauthorizedUserException("Credential not exist");
    }
}
