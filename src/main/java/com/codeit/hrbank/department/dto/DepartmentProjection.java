package com.codeit.hrbank.department.dto;

import com.codeit.hrbank.department.entity.Department;

public interface DepartmentProjection {
    Department getDepartment();
    Long getEmployeeAccount();
}
