package com.codeit.hrbank.change_log.repository;

import com.codeit.hrbank.change_log.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
    Long countByCreatedAtBetween(Instant fromDate, Instant toDate);
}
