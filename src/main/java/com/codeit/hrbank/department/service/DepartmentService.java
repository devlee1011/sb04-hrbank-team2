package com.codeit.hrbank.department.service;

public interface DepartmentService {
    void delete(Long id);

    Long getEmployeeCountByDepartmentId(Long departmentId);
}
