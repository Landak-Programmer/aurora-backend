package com.aurora.backend.repo;

import com.aurora.backend.entity.VoiceCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceCommandRepository extends JpaRepository<VoiceCommand, Long> {

    List<VoiceCommand> findByCommandStartsWith(final String command);
}
