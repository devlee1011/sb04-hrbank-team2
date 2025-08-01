package com.codeit.hrbank.employee.controller;

import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.employee.service.EmployeeService;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final StoredFileService storedFileService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity create(@RequestPart("employeeCreateRequest") EmployeeCreateRequest employeeCreateRequest,
                                 @RequestPart(value = "profile", required = false) MultipartFile profile) {
        Long profileId = Optional.ofNullable(profile)
                .map(file -> {
                    StoredFile StoredFile = storedFileService.createStoredFile(profile);
                    return StoredFile.getId();
                })
                .orElse(null);
        Employee employee = employeeService.create(employeeCreateRequest,profileId);
        EmployeeDto response = employeeMapper.toDto(employee);
        return ResponseEntity.ok(response);
    }
}
