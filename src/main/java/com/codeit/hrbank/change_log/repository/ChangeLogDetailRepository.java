package com.codeit.hrbank.change_log.repository;

import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChangeLogDetailRepository extends JpaRepository<ChangeLogDetail, Long> {
    List<ChangeLogDetail> findAllByChangeLogId(Long id);
}
