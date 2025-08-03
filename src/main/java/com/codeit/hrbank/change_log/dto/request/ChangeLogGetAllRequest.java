package com.codeit.hrbank.change_log.dto.request;

import com.codeit.hrbank.change_log.entity.ChangeLogType;

import java.time.Instant;

public record ChangeLogGetAllRequest(
        String employeeNumber,
        ChangeLogType type,
        String memo,
        String ipAddress,
        Instant atFrom,
        Instant atTo,
        Long idAfter,
        String cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}
