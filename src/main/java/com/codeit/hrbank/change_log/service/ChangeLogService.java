package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;

public interface ChangeLogService {
    void create(EmployeeLogEvent event);
    ChangeLogDetail getChangeLogDetail(Long id);
}
