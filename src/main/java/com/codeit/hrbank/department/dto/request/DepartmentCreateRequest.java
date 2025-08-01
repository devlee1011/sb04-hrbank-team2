package com.codeit.hrbank.department.dto.request;

import java.time.LocalDate;

public record DepartmentCreateRequest(
        String name,
        String description,
        LocalDate establishedDate
) {
}
