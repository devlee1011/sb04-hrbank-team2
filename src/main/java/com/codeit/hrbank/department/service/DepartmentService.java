package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.entity.Department;

public interface DepartmentService {

    Department update(DepartmentUpdateRequest departmentUpdateRequest, Long id);
}
