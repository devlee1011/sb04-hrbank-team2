package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;

public interface EmployeeService {
    Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId);
    Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long profileId);
}
