package com.codeit.hrbank.department.mapper;

import com.codeit.hrbank.department.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "establishedDate",
            expression = "java(departmentCreateRequest.establishedDate() != null ? departmentCreateRequest.establishedDate() : java.time.LocalDate.now())")
    Department toEntity(DepartmentCreateRequest departmentCreateRequest);

    DepartmentDto toDto(Department department, Long employeeCount);
}
