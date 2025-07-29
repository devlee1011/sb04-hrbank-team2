package com.codeit.hrbank.backup.repository;

import com.codeit.hrbank.backup.entitiy.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {
}
