package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Employee getEmployee(Long id);
    Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId);
    Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long profileId);
    void delete(Long id);
    Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest);

}
