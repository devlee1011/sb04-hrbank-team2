package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.employee.projection.EmployeeCountByDepartmentProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    Page<Department> getAllDepartments(DepartmentGetAllRequest pageRequest);

    Department create(Department department);

    Department update(DepartmentUpdateRequest departmentUpdateRequest, Long id);

    Department getDepartment(Long id);

    void delete(Long id);

    Long getEmployeeCountByDepartmentId(Long departmentId);

    List<EmployeeCountByDepartmentProjection> getEmployeeCountsByDepartmentId(List<Long> departmentId);
}
