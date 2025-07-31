package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.employee.specification.EmployeeSpecification;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StoredFileRepository storedFileRepository;

    @Override
    public Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest) {
        String sortField = employeeGetAllRequest.sortField() == null ? "name" : employeeGetAllRequest.sortField();
        Specification<Employee> spec = Specification.unrestricted();
        if ("name".equals(sortField)) {
            spec = spec.and(EmployeeSpecification.greaterThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
        } else if ("hireDate".equals(sortField)) {
            spec = spec.and(EmployeeSpecification.greaterThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
        } else if ("employeeNumber".equals(sortField)) {
            spec = spec.and(EmployeeSpecification.greaterThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
        }
        if (employeeGetAllRequest.nameOrEmail() != null && employeeGetAllRequest.nameOrEmail().contains("@")){
            spec = spec.and(EmployeeSpecification.likeEmail(employeeGetAllRequest.nameOrEmail()));
        }
        else {
            spec = spec.and(EmployeeSpecification.likeName(employeeGetAllRequest.nameOrEmail()));
        }
        spec = spec.and(EmployeeSpecification.likeDepartmentName(employeeGetAllRequest.departmentName()));
        spec = spec.and(EmployeeSpecification.likePosition(employeeGetAllRequest.position()));
        spec = spec.and(EmployeeSpecification.likeEmployeeNumber(employeeGetAllRequest.employeeNumber()));
        spec = spec.and(EmployeeSpecification.betweenHireDate(employeeGetAllRequest.hireDateFrom(),employeeGetAllRequest.hireDateTo()));
        spec = spec.and(EmployeeSpecification.equalStatus(employeeGetAllRequest.status()));

        Pageable pageable = null;
        int pageSize = employeeGetAllRequest.size() == null ? 10 : employeeGetAllRequest.size();
        if (employeeGetAllRequest.sortDirection() == null || employeeGetAllRequest.sortDirection().equals("asc")) {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
        } else {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
        }

        Page<Employee> findList = employeeRepository.findAll(spec,pageable);

        return findList;
    }
}