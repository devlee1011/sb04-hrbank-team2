package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable("id") Long id,
                                   @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        Department department = departmentService.update(departmentUpdateRequest, id);
        DepartmentDto response = departmentMapper.toDto(department, departmentService.getEmployeeCountByDepartmentId(department.getId()));
        return ResponseEntity.ok(response);
    }
}
