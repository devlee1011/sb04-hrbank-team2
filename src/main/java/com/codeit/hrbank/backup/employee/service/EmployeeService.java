package com.codeit.hrbank.backup.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Employee getEmployee(Long id);
    Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId, HttpServletRequest httpServletRequest);
    Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long profileId,HttpServletRequest httpServletRequest);
    void delete(Long id,HttpServletRequest httpServletRequest);
    Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest);

}
