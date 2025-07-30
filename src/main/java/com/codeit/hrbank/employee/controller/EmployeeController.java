package com.codeit.hrbank.employee.controller;

import com.codeit.hrbank.employee.dto.EmployeeDto;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.employee.service.EmployeeService;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final StoredFileService storedFileService;

    @GetMapping
    public ResponseEntity getAll(@RequestParam String nameOrEmail, @RequestParam String employeeNumber,
                                 @RequestParam String departmentName, @RequestParam String position,
                                 @RequestParam LocalDate hireDateFrom, @RequestParam LocalDate hireDateTo,
                                 @RequestParam EmployeeStatus status, @RequestParam Long idAfter,
                                 @RequestParam String cursor, Pageable pageable) {
        return null;
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity create(@RequestPart("employeeCreateRequest") EmployeeCreateRequest employeeCreateRequest,
                                 @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestPart("employeeUpdateRequest") EmployeeUpdateRequest employeeUpdateRequest,
                                 @Parameter(description = "수정할 User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return null;
    }

    @GetMapping("/stats/trend")
    public ResponseEntity getTrend(@RequestParam LocalDate from, @RequestParam LocalDate to,
                                   @RequestParam String unit) {
        return null;
    }

    @GetMapping("/stats/distribution")
    public  ResponseEntity getDistribution(@RequestParam String groupBy, @RequestParam EmployeeStatus status) {
        return null;
    }

    @GetMapping("/count")
    public ResponseEntity getCount(@RequestParam EmployeeStatus status, @RequestParam LocalDate fromDate,
                                   @RequestParam LocalDate toDate) {
        return null;
    }
}
