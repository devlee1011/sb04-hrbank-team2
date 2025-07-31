package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department create(Department department) {
        // 이름 중복 불가
        String name = department.getName();

        if (departmentRepository.existsByName(name)) {
            throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
        }

        // 저장
        return departmentRepository.save(department);
    }
}
