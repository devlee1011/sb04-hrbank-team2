package com.codeit.hrbank.department.dto.response;

import java.time.LocalDate;

public record DepartmentDto(
        Long id,
        String name,
        String description,
        LocalDate establishedDate,
        Long employeeCount
) {
}
