package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.exception.BusinessLogicException;

public interface EmployeeService {
    Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long profileId);
}
