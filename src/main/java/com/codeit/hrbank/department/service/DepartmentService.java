package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.entity.Department;

public interface DepartmentService {
    Department create (Department department);

    Long getEmployeeCountByDepartmentId(Long departmentId);
}
