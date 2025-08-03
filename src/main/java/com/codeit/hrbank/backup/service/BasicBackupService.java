package com.codeit.hrbank.backup.service;

import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.entitiy.BackupStatus;
import com.codeit.hrbank.backup.repository.BackupRepository;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import com.codeit.hrbank.stored_file.service.LocalStoredFileStorage;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBackupService implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final StoredFileRepository storedFileRepository;
  private final LocalStoredFileStorage localStoredFileStorage;

  @Override
  public Backup createBackup(String requestIp) {

    Instant lastBackupStart = backupRepository.findFirstByOrderByStartedAtDesc()
        .map(Backup::getStartedAt)
        .orElse(Instant.EPOCH);

    List<Employee> employees = employeeRepository.findAllByCreatedAtAfter(lastBackupStart);

    Backup backup;

    if (!employees.isEmpty()) {
      long totalCount = backupRepository.count();

      Instant startTime = Instant.now();
      StoredFile storedFile = localStoredFileStorage.backup(employees, totalCount+1, new StoredFile());
      Instant endTime = Instant.now();

      StoredFile savefile = storedFileRepository.save(storedFile);
      backup = Backup.builder()
          .worker(requestIp)
          .status(savefile.getFileName().endsWith(".csv") ? BackupStatus.COMPLETED : BackupStatus.FAILED)
          .startedAt(startTime)
          .endedAt(endTime)
          .storedFile(savefile)
          .build();
    }else{
      backup = Backup.builder()
          .worker(requestIp)
          .status(BackupStatus.SKIPPED)
          .build();
    }

    return backupRepository.save(backup);
  }
}
