package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.DepartmentProjection;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentProjection> getAllDepartments() {
        return departmentRepository.findAllWithEmployeeAccount();
    }
}
