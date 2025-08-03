package com.codeit.hrbank.employee.dto;

import java.time.LocalDate;

public record EmployeeTrendDto(
        LocalDate date,
        long count,
        long change,
        double changeRate
) {
}
