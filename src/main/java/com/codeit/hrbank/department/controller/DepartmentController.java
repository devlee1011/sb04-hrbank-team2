package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
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
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentDto> departmentDtos = departments.stream()
                .map(department -> {
                    return departmentMapper.toDto(
                            department,
                            getEmployeeCount(department)
                    );
                })
                .toList();

        return ResponseEntity.ok(departmentDtos);
    }

    private Long getEmployeeCount(Department department) {
        return department.getEmployees() != null ? department.getEmployees().size() : 0L;
    }
}
