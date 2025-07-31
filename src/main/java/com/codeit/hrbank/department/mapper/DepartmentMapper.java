package com.codeit.hrbank.department.mapper;

import com.codeit.hrbank.department.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity (DepartmentCreateRequest departmentCreateRequest);
    DepartmentDto toDto (Department department, Long employeeCount);
}
