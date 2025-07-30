package com.codeit.hrbank.employee.mapper;

import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDto toDto(Employee employee);
}
