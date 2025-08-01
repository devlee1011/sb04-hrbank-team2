package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department update(DepartmentUpdateRequest departmentUpdateRequest, Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));

        // departmentUpdateRequest에 수정할 항목이 비어있으면 기존 값 유지
        String newName = Optional.ofNullable(departmentUpdateRequest.name())
                .filter(name -> !name.isBlank())
                .orElse(department.getName());

        String newDescription = Optional.ofNullable(departmentUpdateRequest.description())
                .filter(description -> !description.isBlank())
                .orElse(department.getDescription());

        LocalDate newEstablishedDate = Optional.ofNullable(departmentUpdateRequest.establishedDate())
                .orElse(department.getEstablishedDate());

        // 이름이 중복되면 안됨
        if (!newName.equals(department.getName()) && departmentRepository.existsByName(newName)) {
            throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
        }

        department.update(newName, newDescription, newEstablishedDate);
        return departmentRepository.save(department);
    }


}
