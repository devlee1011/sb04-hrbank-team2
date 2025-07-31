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

        String name = department.getName();
        String description = department.getDescription();

        // 이름, 설명 null 불가
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NAME_CANNOT_BE_NULL);
        }

        if (description == null || description.trim().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.DESCRIPTION_CANNOT_BE_NULL);
        }

        // 이름 중복 불가
        if (departmentRepository.existsByName(name)) {
            throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
        }

        // 저장
        return departmentRepository.save(department);
    }
}
