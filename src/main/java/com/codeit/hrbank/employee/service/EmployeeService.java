package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest);
}
