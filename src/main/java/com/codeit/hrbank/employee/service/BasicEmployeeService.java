package com.codeit.hrbank.employee.service;

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

import java.util.HashMap;
import java.util.Map;

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
        Map<String,String> changes = new HashMap<>();
        validateEmployee(id);
        Employee employee = employeeRepository.findById(id).orElse(null);
        employeeRepository.deleteById(id);

        eventPublisher.publishEvent(new EmployeeLogEvent(employee,ChangeLogStatus.DELETE));
    }

    private void validateEmployee(Long id) {
        if(!employeeRepository.existsById(id)) throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND);
    }
}
