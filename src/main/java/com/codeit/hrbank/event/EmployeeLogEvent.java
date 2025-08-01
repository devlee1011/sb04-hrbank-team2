package com.codeit.hrbank.event;

import com.codeit.hrbank.change_log.dto.EventLogDto;
import com.codeit.hrbank.change_log.entity.ChangeLogType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EmployeeLogEvent {
    List<EventLogDto> changelogs;
    ChangeLogType logStatus;
    String ipAddress;
    String memo;
    String employeeNumber;

    public EmployeeLogEvent(List<EventLogDto> logs, ChangeLogType logStatus, String memo, String ipAddress, String employeeNumber) {
        this.changelogs = logs;
        this.logStatus = logStatus;
        this.memo = memo;
        this.ipAddress = ipAddress;
        this.employeeNumber = employeeNumber;
    }
}