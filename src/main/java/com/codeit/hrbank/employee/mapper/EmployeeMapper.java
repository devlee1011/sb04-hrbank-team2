package com.codeit.hrbank.employee.mapper;

import com.codeit.hrbank.employee.dto.EmployeeDistributionDto;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.EmployeeTrendDto;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import com.codeit.hrbank.employee.projection.EmployeeTrendProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "departmentId", expression = "java(employee.getDepartment().getId())")
    @Mapping(target = "departmentName", expression = "java(employee.getDepartment().getName())")
    @Mapping(target = "profileImageId", expression = "java(employee.getProfile() != null ? employee.getProfile().getId() : null)")
    EmployeeDto toDto(Employee employee);

    EmployeeGetAllRequest toGetAllRequest(EmployeeDto dto);

    @Mapping(target = "percentage", expression = "java((double) projection.getCount() / employeeCount)")
    EmployeeDistributionDto toEmployeeDistributionDto(EmployeeDistributionProjection projection, long employeeCount);

    default
    List<EmployeeTrendDto> toEmployeeTrendDtos(List<EmployeeTrendProjection> projections) {
        List<EmployeeTrendDto> employeeTrendDtos = new ArrayList<>();

        for (EmployeeTrendProjection projection : projections) {
            LocalDate date = projection.getTargetDate();
            long currentCount = projection.getCurrentCount();
            long totalCount = projection.getTotalCount();

            long change = (currentCount == 0L) ? 0: totalCount - (totalCount-currentCount);
            double changeRate = (totalCount == 0L) ? 0.0 : (double) change / totalCount;

            EmployeeTrendDto dto = new EmployeeTrendDto(
                    date,
                    totalCount,
                    change,
                    changeRate
            );

            employeeTrendDtos.add(dto);
        }

        return employeeTrendDtos;
    }
}
