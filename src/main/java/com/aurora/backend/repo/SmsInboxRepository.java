package com.aurora.backend.repo;

import com.aurora.backend.entity.SmsInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsInboxRepository extends JpaRepository<SmsInbox, Long> {
}
