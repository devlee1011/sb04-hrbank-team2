package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.dto.response.EmployeeDistributionDto;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface EmployeeService {
    Employee getEmployee(Long id);
    Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId, HttpServletRequest httpServletRequest);
    Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long profileId,HttpServletRequest httpServletRequest);
    void delete(Long id,HttpServletRequest httpServletRequest);
    Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest);

    long getCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);
    EmployeeDistributionDto getDistribution(String groupBy, EmployeeStatus status);
}
