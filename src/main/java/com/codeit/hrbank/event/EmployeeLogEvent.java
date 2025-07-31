package com.codeit.hrbank.event;

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
    ChangeLogStatus logStatus;

    public EmployeeLogEvent(Employee employee, ChangeLogStatus logStatus){
        this.hireDate = employee.getHireDate();
        this.name = employee.getName();
        this.position = employee.getPosition();
        this.departmentName = employee.getDepartment().getName();
        this.email = employee.getEmail();
        this.status = employee.getStatus().toString();
        this.employeeNumber = employee.getEmployeeNumber();
        this.logStatus = logStatus;
    }
}
