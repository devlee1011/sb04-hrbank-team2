package com.codeit.hrbank.employee.controller;

import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final StoredFileService storedFileService;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable long id) {
        Employee employee = employeeService.getEmployee(id);
        EmployeeDto employeeDto = employeeMapper.toDto(employee);
        return ResponseEntity.ok(employeeDto);
    }
}
