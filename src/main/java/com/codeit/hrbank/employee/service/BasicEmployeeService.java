package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public void delete(Long id) {
        Employee employee = validateEmployee(id);
        employeeRepository.deleteById(id);

        eventPublisher.publishEvent(new EmployeeLogEvent(employee, ChangeLogType.DELETE,"직원삭제"));
    }

    private Employee validateEmployee(Long id) {
        return employRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }
}
