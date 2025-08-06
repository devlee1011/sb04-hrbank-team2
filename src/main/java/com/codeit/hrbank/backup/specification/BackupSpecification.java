package com.codeit.hrbank.backup.specification;

import com.codeit.hrbank.backup.dto.request.BackupGetAllRequest;
import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.entitiy.BackupStatus;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.data.jpa.domain.Specification;

public class BackupSpecification {
  public static Specification<Backup> conditionFilters(BackupGetAllRequest request) {
    return (root, query, cb) -> {
      Predicate predicates = cb.conjunction();

      if (request.worker() != null && !request.worker().isBlank()) {
        predicates.getExpressions().add(cb.like(root.get("worker"), request.worker()));
      }

      if (request.status() != null && !request.status().isBlank()) {
        predicates.getExpressions()
            .add(cb.equal(root.get("status"), request.status()));
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