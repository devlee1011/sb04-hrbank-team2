package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.entity.Employee;

public interface EmployeeService {
    Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId);
}
