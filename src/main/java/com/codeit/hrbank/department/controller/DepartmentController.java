package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.base.dto.PageResponse;
import com.codeit.hrbank.department.dto.DepartmentProjection;
import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.response.CursorPageResponseDepartmentDto;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity<CursorPageResponseDepartmentDto<DepartmentDto>> getAll(@ModelAttribute("departmentGetAllRequest") DepartmentGetAllRequest departmentGetAllRequest) {
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
