package com.codeit.hrbank.backup.dto.request;

import java.time.LocalDate;

public record BackupGetAllRequest(
    String worker,
    String status,
    LocalDate startedAtFrom,
    LocalDate startedAtTo,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {}
