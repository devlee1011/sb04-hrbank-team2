package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final StoredFileRepository storedFileRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId) {
        isDuplicateEmail(employeeCreateRequest.email());
        if (profileId == null) {
            System.out.println("이미지가 포함되지 않아 기본 프로필로 설정됩니다.");
        }
        StoredFile profile = Optional.ofNullable(profileId)
                .flatMap(storedFileRepository::findById)
                .orElse(null);
        Department department = departmentRepository.findById(employeeCreateRequest.departmentId()).orElse(null);
        Employee employee = new Employee(
                employeeCreateRequest.name(), employeeCreateRequest.email(),
                department, employeeCreateRequest.position(),
                employeeCreateRequest.hireDate(), profile
        );
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setEmployeeNumber(
                String.format("EMP-%d-%03d",
                                savedEmployee.getHireDate().getYear(),
                                savedEmployee.getId())
        );
        employeeRepository.save(savedEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(employee, ChangeLogType.CREATE,employeeCreateRequest.memo()));
        return savedEmployee;
    }

    private void isDuplicateEmail(String email) {
        if (employeeRepository.existsByEmail(email))
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
    }
}
