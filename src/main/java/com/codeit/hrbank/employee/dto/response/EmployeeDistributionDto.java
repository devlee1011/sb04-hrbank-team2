package com.codeit.hrbank.employee.dto.response;

public record EmployeeDistributionDto(
        String groupBy,
        long count,
        double percentage
) {
}
