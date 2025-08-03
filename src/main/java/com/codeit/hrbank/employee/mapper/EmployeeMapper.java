package com.codeit.hrbank.employee.mapper;

import com.codeit.hrbank.employee.dto.EmployeeDistributionDto;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.EmployeeTrendDto;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    List<EmployeeTrendDto> toEmployeeTrendDto(Map<LocalDate, Long> countMap, String unit) {
        List<EmployeeTrendDto> employeeTrendDtos = new ArrayList<>();

        long prev = 0L;

        for (Map.Entry<LocalDate, Long> entry : countMap.entrySet()) {
            LocalDate date = entry.getKey();
            long count = entry.getValue();
            long change = count - prev;
            double changeRate = (prev == 0L) ? 0.0 : (double) change / prev;

            EmployeeTrendDto dto = new EmployeeTrendDto(
                    date,
                    count,
                    change,
                    changeRate
            );

            employeeTrendDtos.add(dto);
            prev = count;
        }

        return employeeTrendDtos;
    }
}
