package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.department.specification.DepartmentSpecification;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    //
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Department> getAllDepartments(DepartmentGetAllRequest departmentGetAllRequest) {
        // 검색 필드, 정렬 방향 초기화
        String sortField = departmentGetAllRequest.sortField() == null || departmentGetAllRequest.sortField().isEmpty() ? "name" : departmentGetAllRequest.sortField();
        String sortDirection = departmentGetAllRequest.sortDirection() == null ? "asc" : departmentGetAllRequest.sortDirection();

        Specification<Department> spec = Specification.unrestricted();

        // 정렬 방향 설정
        if ("asc".equalsIgnoreCase(sortDirection)) {
            switch (sortField) {
                case "name" -> {
                    spec = spec.and(DepartmentSpecification.greaterThanName(
                            departmentGetAllRequest.idAfter(),
                            departmentGetAllRequest.cursor())
                    );
                }
                case "establishedDate" -> {
                    spec = spec.and(DepartmentSpecification.greaterThanEstablishedDate(
                            departmentGetAllRequest.idAfter(),
                            LocalDate.parse(departmentGetAllRequest.cursor()))
                    );
                }
            }
        } else if ("desc".equalsIgnoreCase(sortDirection)) {
            switch (sortField) {
                case "name" -> {
                    spec = spec.and(DepartmentSpecification.lessThanName(
                            departmentGetAllRequest.idAfter(),
                            departmentGetAllRequest.cursor())
                    );
                }
                case "establishedDate" -> {
                    spec = spec.and(DepartmentSpecification.lessThanEstablishedDate(
                            departmentGetAllRequest.idAfter(),
                            LocalDate.parse(departmentGetAllRequest.cursor()))
                    );
                }
            }
        }

        // 검색 조건
        spec = spec.and(DepartmentSpecification.likeName(departmentGetAllRequest.nameOrDescription()));
        spec = spec.and(DepartmentSpecification.likeDescription(departmentGetAllRequest.nameOrDescription()));

        // Pageable
        Pageable pageable = null;
        int pageSize = departmentGetAllRequest.size() == null ? 10 : departmentGetAllRequest.size();
        switch(sortDirection.toLowerCase()) {
            case "asc" -> {
                pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
            }
            case "desc" -> {
                pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
            }
            default -> pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
        }
        return departmentRepository.findAll(spec, pageable);
    }

    @Override
    public Long getEmployeeCountByDepartmentId(Long departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }
}