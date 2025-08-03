package com.codeit.hrbank.employee.mapper;

import com.codeit.hrbank.employee.dto.EmployeeDistributionDto;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "departmentId", expression = "java(employee.getDepartment().getId())")
    @Mapping(target = "departmentName", expression = "java(employee.getDepartment().getName())")
    @Mapping(target = "profileImageId", expression = "java(employee.getProfile() != null ? employee.getProfile().getId() : null)")
    EmployeeDto toDto(Employee employee);

    EmployeeGetAllRequest toGetAllRequest(EmployeeDto dto);

    @Mapping(target = "percentage", expression = "java((double) projection.getCount() / employeeCount)")
    EmployeeDistributionDto toEmployeeDistributionDto(EmployeeDistributionProjection projection, long employeeCount);
}
