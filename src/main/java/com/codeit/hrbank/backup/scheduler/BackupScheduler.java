package com.codeit.hrbank.backup.scheduler;

import com.codeit.hrbank.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackupScheduler {

  private final BackupService backupService;

  @Scheduled(cron = "${backup.schedule.cron}")
  public void ScheduledBackup() {
    backupService.createBackup("system");
  }
}

