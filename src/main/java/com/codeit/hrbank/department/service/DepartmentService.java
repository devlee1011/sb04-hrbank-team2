package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.entity.Department;

public interface DepartmentService {
    Department getDepartment(Long id);

    Long getEmployeeCountByDepartmentId(Long departmentId);
}
