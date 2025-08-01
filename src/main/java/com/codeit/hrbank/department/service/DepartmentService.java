package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.DepartmentProjection;
import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;

import java.util.List;

public interface DepartmentService {
    List<DepartmentProjection> getAllDepartments(DepartmentGetAllRequest pageRequest);
}
