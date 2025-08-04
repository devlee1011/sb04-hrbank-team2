package com.codeit.hrbank.employee.dto;

public record EmployeeDistributionDto(
        String groupKey,
        long count,
        double percentage
) {
}
