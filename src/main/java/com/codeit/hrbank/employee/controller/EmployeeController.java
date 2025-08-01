package com.codeit.hrbank.employee.controller;

import com.codeit.hrbank.base.dto.PageResponse;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.employee.service.EmployeeService;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final StoredFileService storedFileService;

    @GetMapping
    public ResponseEntity getAll(@ModelAttribute("employeeGetAllRequest") EmployeeGetAllRequest employeeGetAllRequest) {
        Page<Employee> employees = employeeService.getAll(employeeGetAllRequest);
        if(!employees.hasContent()) {
            return ResponseEntity.ok(PageResponse.from(Page.empty(),null,null));
        }
        Long idAfter = employees.getContent().get(employees.getContent().size() - 1).getId();
        String cursor = null;
        if ("hireDate".equals(employeeGetAllRequest.sortField())) {
            cursor = employees.getContent().get(employees.getContent().size() - 1).getHireDate().toString();
        } else if ("employeeNumber".equals(employeeGetAllRequest.sortField())) {
            cursor = employees.getContent().get(employees.getContent().size() - 1).getEmployeeNumber();
        } else {
            cursor = employees.getContent().get(employees.getContent().size() - 1).getName();
        }

        Page<EmployeeDto> employeeDtos = employees
                .map(employeeMapper::toDto);
        PageResponse response = PageResponse.from(employeeDtos, idAfter, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable long id) {
        Employee employee = employeeService.getEmployee(id);
        EmployeeDto employeeDto = employeeMapper.toDto(employee);
        return ResponseEntity.ok(employeeDto);
    }
}
