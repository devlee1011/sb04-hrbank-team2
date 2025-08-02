package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    //
    private final EmployeeRepository employeeRepository;

    @Override
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));

        // 소속 직원이 있는 부서는 삭제 불가
        if (getEmployeeCountByDepartmentId(department.getId()) > 0) {
            throw new BusinessLogicException(ExceptionCode.DEPARTMENT_HAS_EMPLOYEE_CANNOT_DELETE);
        }

        departmentRepository.deleteById(id);
    }


    @Override
    public Long getEmployeeCountByDepartmentId(Long departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }
}