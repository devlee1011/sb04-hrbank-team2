package com.codeit.hrbank.event;

import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EmployeeLogEvent {
    LocalDate hireDate;
    String name;
    String position;
    String departmentName;
    String email;
    String status;
    String employeeNumber;
    ChangeLogType logStatus;
    String memo;

    public EmployeeLogEvent(Employee employee, ChangeLogType logStatus, String memo){
        this.hireDate = employee.getHireDate();
        this.name = employee.getName();
        this.position = employee.getPosition();
        this.departmentName = employee.getDepartment().getName();
        this.email = employee.getEmail();
        this.status = employee.getStatus().toString();
        this.employeeNumber = employee.getEmployeeNumber();
        this.logStatus = logStatus;
        this.memo = memo;
    }

    public EmployeeLogEvent(EmployeeUpdateRequest employeeUpdateRequest, String departmentName){
        this.hireDate = employeeUpdateRequest.hireDate();
        this.name = employeeUpdateRequest.name();
        this.position = employeeUpdateRequest.position();
        this.departmentName = departmentName;
        this.email = employeeUpdateRequest.email();
        this.status = employeeUpdateRequest.status().toString();
        this.employeeNumber = null;
        this.logStatus = ChangeLogType.UPDATE;
        this.memo = employeeUpdateRequest.memo();
    }
}
