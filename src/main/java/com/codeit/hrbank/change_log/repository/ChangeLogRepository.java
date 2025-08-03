package com.codeit.hrbank.change_log.repository;

import com.codeit.hrbank.change_log.entity.ChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
    Page<ChangeLog> findAll(Specification<ChangeLog> spec, Pageable pageable);
    Long countByCreatedAtBetween(Instant fromDate, Instant toDate);
}
