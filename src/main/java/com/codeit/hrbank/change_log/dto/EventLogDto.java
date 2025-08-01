package com.codeit.hrbank.change_log.dto;

public record EventLogDto(
        String fieldName,
        String oldValue,
        String newValue
) {
}
