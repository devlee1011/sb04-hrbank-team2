package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    //
    private final EmployeeRepository employeeRepository;

    @Override
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));
    }

    @Override
    public Long getEmployeeCountByDepartmentId(Long departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }
}