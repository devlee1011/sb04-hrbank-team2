package com.codeit.hrbank.department.controller;

import com.codeit.hrbank.department.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.dto.response.CursorPageResponseDepartmentDto;
import com.codeit.hrbank.department.dto.response.DepartmentDto;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.mapper.DepartmentMapper;
import com.codeit.hrbank.department.service.DepartmentService;
import com.codeit.hrbank.employee.projection.EmployeeCountByDepartmentProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity getAll(@ModelAttribute("departmentGetAllRequest") DepartmentGetAllRequest departmentGetAllRequest) {
        Page<Department> departments = departmentService.getAllDepartments(departmentGetAllRequest);
        if(!departments.hasContent() || departments.getContent().isEmpty()) {
            return ResponseEntity.ok(CursorPageResponseDepartmentDto.from(Page.empty(), null, null));
        }

        Long idAfter = departments.getContent().get(departments.getContent().size() - 1).getId();

        String cursor = null;
        switch(StringUtils.hasText(departmentGetAllRequest.sortField()) ? departmentGetAllRequest.sortField() : "establishedDate") {
            case "name" -> cursor = departments.getContent().get(departments.getContent().size() - 1).getName();
            case "establishedDate" -> cursor = departments.getContent().get(departments.getContent().size() - 1).getEstablishedDate().toString();
        }

        List<Long> departmentIds = departments.getContent().stream().map(Department::getId).toList();
        List<EmployeeCountByDepartmentProjection> projections = departmentService.getEmployeeCountsByDepartmentId(departmentIds);
        Map<Long, Long> employeeCountMap = toEmployeeCountMap(projections);

        Page<DepartmentDto> page = departments.map(department ->
                departmentMapper.toDto(department, employeeCountMap.getOrDefault(department.getId(), 0L))
        );

        CursorPageResponseDepartmentDto<DepartmentDto> response = CursorPageResponseDepartmentDto.from(page, idAfter, cursor);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        Department department = departmentService.create(departmentMapper.toEntity(departmentCreateRequest));
        Long employeeCount = departmentService.getEmployeeCountByDepartmentId(department.getId());
        DepartmentDto departmentDto = departmentMapper.toDto(department, employeeCount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(departmentDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        Department department = departmentService.update(departmentUpdateRequest, id);
        DepartmentDto response = departmentMapper.toDto(department, departmentService.getEmployeeCountByDepartmentId(department.getId()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Department department = departmentService.getDepartment(id);
        DepartmentDto departmentDto = departmentMapper.toDto(department, departmentService.getEmployeeCountByDepartmentId(department.getId()));
        return ResponseEntity.ok(departmentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<Long, Long> toEmployeeCountMap(List<EmployeeCountByDepartmentProjection> projections) {
        return projections.stream()
                .collect(Collectors.toMap(
                        EmployeeCountByDepartmentProjection::getDepartmentId,
                        EmployeeCountByDepartmentProjection::getEmployeeCount
                ));
    }
}
