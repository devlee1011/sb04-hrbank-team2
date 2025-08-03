package com.codeit.hrbank.backup.employee.controller;

import com.codeit.hrbank.base.dto.PageResponse;
import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.employee.service.EmployeeService;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
            return ResponseEntity.ok(PageResponse.from(Page.empty(),null,null));
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

    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity create(@RequestPart("employeeCreateRequest") EmployeeCreateRequest employeeCreateRequest,
                                 @RequestPart(value = "profile", required = false) MultipartFile profile,
                                 HttpServletRequest httpServletRequest) {
        Long profileId = Optional.ofNullable(profile)
                .map(file -> {
                    StoredFile storedFile = storedFileService.createStoredFile(profile);
                    return storedFile.getId();
                })
                .orElse(null);
        Employee employee = employeeService.create(employeeCreateRequest,profileId,httpServletRequest);
        EmployeeDto response = employeeMapper.toDto(employee);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestPart("employeeUpdateRequest") EmployeeUpdateRequest employeeUpdateRequest,
                                 @Parameter(description = "수정할 User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile,
                                 HttpServletRequest httpServletRequest) {
        Long storedFileId = Optional.ofNullable(profile)
                .map(file -> {
                    StoredFile StoredFile = storedFileService.createStoredFile(profile);
                    return StoredFile.getId();
                })
                .orElse(null);
        Employee employee = employeeService.update(id,employeeUpdateRequest, storedFileId,httpServletRequest);
        EmployeeDto response = employeeMapper.toDto(employee);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable long id) {
        Employee employee = employeeService.getEmployee(id);
        EmployeeDto employeeDto = employeeMapper.toDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id,
                                 HttpServletRequest httpServletRequest) {
        employeeService.delete(id,httpServletRequest);
        return ResponseEntity.noContent().build();
    }
}
