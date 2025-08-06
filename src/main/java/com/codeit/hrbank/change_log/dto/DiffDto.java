package com.codeit.hrbank.change_log.dto;

public record DiffDto(
        String propertyName,
        String before,
        String after
) {
}
