package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import com.codeit.hrbank.change_log.repository.ChangeLogDetailRepository;
import com.codeit.hrbank.change_log.repository.ChangeLogRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.event.EmployeeLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BasicChangeLogService implements ChangeLogService {
    private final ChangeLogRepository changeLogRepository;
    private final ChangeLogDetailRepository changeLogDetailRepository;
    private final EmployeeRepository employeeRepository;

    public void create(EmployeeLogEvent event) {
        ChangeLog changeLog = changeLogRepository.save(new ChangeLog(event.getLogStatus(),event.getEmployeeNumber(), event.getMemo(), event.getIpAddress()));
        List<ChangeLogDetail> details = createDetailLogs(event,changeLog);
        changeLogDetailRepository.saveAll(details);
    }

    private List<ChangeLogDetail> createDetailLogs(EmployeeLogEvent event, ChangeLog changeLog) {
        List<ChangeLogDetail> details = new ArrayList<>();
        for (DiffDto c : event.getChangelogs()) {
            if (!Objects.equals(c.before(), c.after())) {
                details.add(new ChangeLogDetail(changeLog, c.propertyName(),c.before(),c.after()));
            }
        }
        return details;
    }
}
