package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.change_log.repository.ChangeLogDetailRepository;
import com.codeit.hrbank.change_log.repository.ChangeLogRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.event.EmployeeLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChangeLogService implements ChangeLogService {
    private final ChangeLogRepository changeLogRepository;
    private final ChangeLogDetailRepository changeLogDetailRepository;
    private final EmployeeRepository employeeRepository;

    public void create(EmployeeLogEvent event) {
        if(event.getLogStatus() == ChangeLogType.CREATE) {
            ChangeLog changeLog = new ChangeLog(event.getLogStatus(),event.getEmployeeNumber(), event.getMemo() );
        } else if (event.getLogStatus() == ChangeLogType.UPDATE) {

        } else if (event.getLogStatus() == ChangeLogType.DELETE) {

        }
    }
}
