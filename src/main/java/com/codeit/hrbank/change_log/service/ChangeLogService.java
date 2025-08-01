package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.event.EmployeeLogEvent;

public interface ChangeLogService {
    void create(EmployeeLogEvent event);
}
