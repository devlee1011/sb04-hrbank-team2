package com.codeit.hrbank.employee.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EmployeeTrendProjection {
    private final LocalDate targetDate;
    private final long currentCount;
    private final long prevCount;
}
