package com.codeit.hrbank.backup.repository;

import com.codeit.hrbank.backup.entitiy.Backup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BackupRepository extends JpaRepository<Backup, Long>,
    JpaSpecificationExecutor<Backup> {
  Optional<Backup> findFirstByOrderByStartedAtDesc();
}
