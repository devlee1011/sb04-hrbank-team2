package com.codeit.hrbank.backup.service;

import com.codeit.hrbank.backup.dto.request.BackupGetAllRequest;
import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.entitiy.BackupStatus;
import com.codeit.hrbank.backup.repository.BackupRepository;
import com.codeit.hrbank.backup.specification.BackupSpecification;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import com.codeit.hrbank.stored_file.service.LocalStoredFileStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBackupService implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final StoredFileRepository storedFileRepository;
  private final LocalStoredFileStorage localStoredFileStorage;

  @Override
  @Transactional
  public Backup createBackup(String requestIp) {

    Instant lastBackupStart = backupRepository.findFirstByOrderByStartedAtDesc()
        .map(Backup::getStartedAt)
        .orElse(Instant.EPOCH);

    List<Employee> employees = employeeRepository.findAllByCreatedAtAfter(lastBackupStart);

    Backup backup;

    Instant startTime = Instant.now();
    Instant endTime;

    if (!employees.isEmpty()) {
      long totalCount = backupRepository.count();

      StoredFile storedFile = localStoredFileStorage.backup(employees, totalCount+1, new StoredFile());
      endTime = Instant.now();

      StoredFile savefile = storedFileRepository.save(storedFile);
      backup = Backup.builder()
          .worker(requestIp)
          .status(savefile.getFileName().endsWith(".csv") ? BackupStatus.COMPLETED : BackupStatus.FAILED)
          .startedAt(startTime)
          .endedAt(endTime)
          .storedFile(savefile)
          .build();
    }else{
      endTime = Instant.now();

      backup = Backup.builder()
          .worker(requestIp)
          .status(BackupStatus.SKIPPED)
          .startedAt(startTime)
          .endedAt(endTime)
          .build();
    }

    return backupRepository.save(backup);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Backup> getAllBackups(BackupGetAllRequest request) {
    Specification<Backup> spec = BackupSpecification.conditionFilters(request);

    int size = request.size() != null && request.size() > 0 ? request.size() : 10;

    String sortField = request.sortField() != null ? request.sortField() : "startedAt";
    Sort.Direction direction =
        "ASC".equalsIgnoreCase(request.sortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(direction, sortField));

    return backupRepository.findAll(spec, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Backup getLatestBackup(String statusString) {
    BackupStatus status = BackupStatus.valueOf(statusString.toUpperCase());
    return backupRepository.findFirstByStatusOrderByIdDesc(status)
        .orElseThrow(() -> new NoSuchElementException("해당 상태의 백업 기록이 없습니다."));
  }
}
