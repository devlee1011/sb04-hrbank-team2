package com.codeit.hrbank.backup.service;

import com.codeit.hrbank.backup.entitiy.Backup;

public interface BackupService {
  Backup createBackup(String requestIp);
}
