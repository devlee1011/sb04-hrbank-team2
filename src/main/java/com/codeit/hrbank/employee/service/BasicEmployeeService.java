package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StoredFileRepository storedFileRepository;
}
