package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;

import com.codeit.hrbank.change_log.dto.request.ChangeLogGetAllRequest;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

public interface ChangeLogService {
    Page<ChangeLog> getAll(ChangeLogGetAllRequest changeLogGetAllRequest);
    void create(EmployeeLogEvent event);
    List<ChangeLogDetail> getChangeLogDetail(Long id);
    Long getCount(Instant fromDate, Instant toDate);
}
