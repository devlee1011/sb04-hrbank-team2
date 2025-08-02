package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        Department department = departmentService.create(departmentMapper.toEntity(departmentCreateRequest));
        Long employeeCount = departmentService.getEmployeeCountByDepartmentId(department.getId());
        DepartmentDto departmentDto = departmentMapper.toDto(department, employeeCount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(departmentDto);
    }
}
