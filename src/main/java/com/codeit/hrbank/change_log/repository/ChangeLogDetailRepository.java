package com.codeit.hrbank.change_log.repository;

import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogDetailRepository extends JpaRepository<ChangeLogDetail, Long> {
}
