package com.codeit.hrbank.change_log.dto;

import com.codeit.hrbank.change_log.entity.ChangeLogType;

import java.time.Instant;

public record ChangeLogDto(
        Long id,
        ChangeLogType type,
        String employeeNumber,
        String memo,
        String ipAddress,
        Instant at
) {
}
