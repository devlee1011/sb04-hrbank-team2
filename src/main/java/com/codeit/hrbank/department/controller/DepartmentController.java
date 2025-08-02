package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Department department = departmentService.getDepartment(id);
        DepartmentDto departmentDto = departmentMapper.toDto(department, departmentService.getEmployeeCountByDepartmentId(department.getId()));
        return ResponseEntity.ok(departmentDto);
    }
}
