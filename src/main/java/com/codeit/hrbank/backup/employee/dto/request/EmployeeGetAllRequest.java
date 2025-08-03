package com.codeit.hrbank.backup.employee.dto.request;

import com.codeit.hrbank.employee.entity.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeGetAllRequest(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        EmployeeStatus status,
        Long idAfter,
        String cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}
