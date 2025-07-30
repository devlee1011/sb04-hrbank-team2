package com.codeit.hrbank.employee.dto.request;

import com.codeit.hrbank.employee.entity.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        EmployeeStatus status,
        String memo
) {
}
