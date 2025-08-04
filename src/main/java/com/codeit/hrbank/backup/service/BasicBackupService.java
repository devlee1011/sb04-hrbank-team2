package com.codeit.hrbank.backup.service;

import com.codeit.hrbank.backup.dto.request.BackupGetAllRequest;
import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.entitiy.BackupStatus;
import com.codeit.hrbank.backup.repository.BackupRepository;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import com.codeit.hrbank.stored_file.service.LocalStoredFileStorage;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

  @Override
  public Page<Backup> getAllBackups(BackupGetAllRequest request) {
    Specification<Backup> spec = withFilters(request);

    int size = request.size() != null && request.size() > 0 ? request.size() : 20;

    String sortField = request.sortField() != null ? request.sortField() : "id";
    Sort.Direction direction =
        "desc".equalsIgnoreCase(request.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(direction, sortField));

    return backupRepository.findAll(spec, pageable);
  }

  private Specification<Backup> withFilters(BackupGetAllRequest request) {
    return (root, query, cb) -> {
      var predicates = cb.conjunction();

      if (request.worker() != null && !request.worker().isBlank()) {
        predicates.getExpressions().add(cb.equal(root.get("worker"), request.worker()));
      }

      if (request.status() != null && !request.status().isBlank()) {
        predicates.getExpressions()
            .add(cb.equal(root.get("status"), BackupStatus.valueOf(request.status())));
      }

      if (request.startedAtFrom() != null) {
        Instant from = request.startedAtFrom().atStartOfDay().toInstant(ZoneOffset.UTC);
        predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("startedAt"), from));
      }

      if (request.startedAtTo() != null) {
        Instant to = request.startedAtTo().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        predicates.getExpressions().add(cb.lessThan(root.get("startedAt"), to));
      }

      if (request.idAfter() != null) {
        predicates.getExpressions().add(cb.greaterThan(root.get("id"), request.idAfter()));
      }

      return predicates;
    };
  }
}
