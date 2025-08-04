package com.codeit.hrbank.backup.service;

import com.codeit.hrbank.backup.dto.request.BackupGetAllRequest;
import com.codeit.hrbank.backup.entitiy.Backup;
import org.springframework.data.domain.Page;

public interface BackupService {
  Backup createBackup(String requestIp);

  Page<Backup> getAllBackups(BackupGetAllRequest backupGetAllRequest);
}
