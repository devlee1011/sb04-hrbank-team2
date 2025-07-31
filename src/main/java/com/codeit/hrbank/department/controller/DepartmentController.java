package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.DepartmentProjection;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentProjection> departmentProjections = departmentService.getAllDepartments();
        List<DepartmentDto> departmentDtos = departmentProjections.stream()
                .map(departmentProjection -> departmentMapper.toDto(
                        departmentProjection.getDepartment(),
                        departmentProjection.getEmployeeCount()
                ))
                .toList();

        return ResponseEntity.ok(departmentDtos);
    }
}
