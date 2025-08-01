package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest);
    Employee getEmployee(Long id);
}
