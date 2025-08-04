package com.codeit.hrbank.backup.repository;

import com.codeit.hrbank.backup.entitiy.Backup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {
  Optional<Backup> findFirstByOrderByStartedAtDesc();
}
