package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.DepartmentProjection;

import java.util.List;

public interface DepartmentService {
    List<DepartmentProjection> getAllDepartments();
}
