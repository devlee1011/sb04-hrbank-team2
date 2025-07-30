package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public void delete(Long id) {
        validateEmployee(id);
        employeeRepository.deleteById(id);
    }

    private void validateEmployee(Long id) {
        if(!employeeRepository.existsById(id)) throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND);
    }
}
