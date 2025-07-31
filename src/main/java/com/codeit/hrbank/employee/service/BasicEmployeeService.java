package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
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

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StoredFileRepository storedFileRepository;

    @Transactional
    @Override
    public Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long newProfileId) {
        isDuplicateEmail(employeeUpdateRequest.email());
        isDuplicateDepartment(employeeUpdateRequest.departmentId());

        Employee findEmployee = employeeRepository.findById(id).orElse(null);
        Department findDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId()).orElse(null);

        Optional.ofNullable(employeeUpdateRequest.name())
                .ifPresent(findEmployee::setName);
        Optional.ofNullable(employeeUpdateRequest.email())
                .ifPresent(findEmployee::setEmail);

        Optional.ofNullable(employeeUpdateRequest.departmentId())
                .ifPresent(departmentId -> {
                    findEmployee.setDepartment(findDepartment);
                });

        Optional.ofNullable(employeeUpdateRequest.position())
                .ifPresent(findEmployee::setPosition);
        Optional.ofNullable(employeeUpdateRequest.hireDate())
                .ifPresent(findEmployee::setHireDate);
        Optional.ofNullable(employeeUpdateRequest.status())
                .ifPresent(findEmployee::setStatus);

        Optional.ofNullable(newProfileId).ifPresent(storedFileId -> {  // 변경할 프로필이 있으면 삭제 후 등록
            Optional.ofNullable(findEmployee.getProfile()).ifPresent(storedFileRepository::delete);
            StoredFile profile = storedFileRepository.findById(newProfileId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORED_FILE_NOT_FOUND));
            findEmployee.setProfile(profile);
        });

        Employee employee = employeeRepository.save(findEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(employeeUpdateRequest,findDepartment.getName()));
        return employee;
    }

    private void isDuplicateEmail(String email) {
        if(!employeeRepository.existsByEmail(email)) throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
    }

    private void isDuplicateDepartment(Long departmentId) {
        if(!departmentRepository.existsById(departmentId)) throw new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND);
    }
}
