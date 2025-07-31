package com.codeit.hrbank.employee.controller;

import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.dto.response.PageResponse;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.employee.service.EmployeeService;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return ResponseEntity.noContent().build();
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
}
