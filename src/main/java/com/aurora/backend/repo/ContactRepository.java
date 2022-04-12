package com.aurora.backend.repo;

import com.aurora.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact findByName(final String name);

    List<Contact> findByNameStartsWith(final String name);

    List<Contact> findByNameEndsWith(final String name);

    List<Contact> findByNameContaining(final String name);
}
