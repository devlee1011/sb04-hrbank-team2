package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.response.CursorPageResponseDepartmentDto;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute("departmentGetAllRequest") DepartmentGetAllRequest departmentGetAllRequest) {
        Page<Department> departments = departmentService.getAllDepartments(departmentGetAllRequest);
        if(!departments.hasContent() || departments.getContent().isEmpty()) {
            return ResponseEntity.ok(CursorPageResponseDepartmentDto.from(Page.empty(), null, null));
        }

        Long idAfter = departments.getContent().get(departments.getContent().size() - 1).getId();

        String cursor = null;
        switch(departmentGetAllRequest.sortField()) {
            case "name" -> {
                cursor = departments.getContent().get(departments.getContent().size() - 1).getName();
            }
            case "establishedDate" -> {
                cursor = departments.getContent().get(departments.getContent().size() - 1).getEstablishedDate().toString();
            }
        }

        Page<DepartmentDto> departmentDtos = departments
                .map(department -> departmentMapper.toDto(department, departmentService.getEmployeeCountByDepartmentId(department.getId())));

        CursorPageResponseDepartmentDto response = CursorPageResponseDepartmentDto.from(departmentDtos, idAfter, cursor);
        return ResponseEntity.ok(response);
    }
}
