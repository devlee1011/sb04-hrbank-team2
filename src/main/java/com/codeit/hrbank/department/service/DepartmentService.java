package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.entity.Department;
import org.springframework.data.domain.Page;

public interface DepartmentService {
    Page<Department> getAllDepartments(DepartmentGetAllRequest pageRequest);

    Long getEmployeeCountByDepartmentId(Long departmentId);
}
