package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.entity.Department;
import org.springframework.data.domain.Page;

public interface DepartmentService {
    Page<Department> getAllDepartments(DepartmentGetAllRequest pageRequest);

    Department create(Department department);

    Department update(DepartmentUpdateRequest departmentUpdateRequest, Long id);

    Department getDepartment(Long id);

    void delete(Long id);

    Long getEmployeeCountByDepartmentId(Long departmentId);
}
