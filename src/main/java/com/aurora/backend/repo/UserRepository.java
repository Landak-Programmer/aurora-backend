package com.aurora.backend.repo;

import com.aurora.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(final String username);

    User findByEmail(final String username);

    User findByPhoneNumber(final String phoneNumber);

    User findByTokenAndActive(String apiKey, boolean active);

    User findByUsernameAndPasswordAndActive(String username, String password, boolean active);

}
