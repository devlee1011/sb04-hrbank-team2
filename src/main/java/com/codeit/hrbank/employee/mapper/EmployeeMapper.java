package com.codeit.hrbank.employee.mapper;

import com.codeit.hrbank.employee.dto.EmployeeDistributionDto;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.EmployeeTrendDto;
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

    @Mapping(target = "percentage", expression = "java(employeeCount == 0 ? 0.0 : ((double) projection.getCount() / employeeCount) * 100.0)")
    EmployeeDistributionDto toEmployeeDistributionDto(EmployeeDistributionProjection projection, long employeeCount);

    default
    List<EmployeeTrendDto> toEmployeeTrendDtos(List<EmployeeTrendProjection> projections) {
        List<EmployeeTrendDto> employeeTrendDtos = new ArrayList<>();

        for (EmployeeTrendProjection projection : projections) {
            LocalDate date = projection.getTargetDate();
            long currentCount = projection.getCurrentCount();
            long prevCount = projection.getPrevCount();

            long change = (prevCount == 0L) ? 0L : currentCount - prevCount;
            double changeRate = (prevCount == 0L) ? 0.0 : (double) change / prevCount;

            EmployeeTrendDto dto = new EmployeeTrendDto(
                    date,
                    currentCount,
                    change,
                    changeRate*100.0
            );

            employeeTrendDtos.add(dto);
        }

        return employeeTrendDtos;
    }
}
