package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.department.specification.DepartmentSpecification;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    //
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Department> getAllDepartments(DepartmentGetAllRequest departmentGetAllRequest) {
        // 정렬 조건, 정렬 방향 초기화
        String sortField = !StringUtils.hasText(departmentGetAllRequest.sortField()) ? "establishedDate" : departmentGetAllRequest.sortField();
        String sortDirection = !StringUtils.hasText(departmentGetAllRequest.sortDirection()) ? "asc" : departmentGetAllRequest.sortDirection();
        String cursorRaw = departmentGetAllRequest.cursor();
        LocalDate cursorDate = getCursorDate(cursorRaw, sortField, sortDirection);

        Specification<Department> spec = Specification.unrestricted();

        // 정렬 방향 설정
        if ("asc".equalsIgnoreCase(sortDirection)) {
            switch (sortField) {
                case "name" -> spec = spec.and(DepartmentSpecification.greaterThanName(
                        departmentGetAllRequest.idAfter(),
                        departmentGetAllRequest.cursor())
                );

                case "establishedDate" -> spec = spec.and(DepartmentSpecification.greaterThanEstablishedDate(
                        departmentGetAllRequest.idAfter(),
                        cursorDate
                ));

            }
        } else if ("desc".equalsIgnoreCase(sortDirection)) {
            switch (sortField) {
                case "name" -> spec = spec.and(DepartmentSpecification.lessThanName(
                        departmentGetAllRequest.idAfter(),
                        departmentGetAllRequest.cursor())
                );
                case "establishedDate" -> spec = spec.and(DepartmentSpecification.lessThanEstablishedDate(
                        departmentGetAllRequest.idAfter(),
                        cursorDate
                ));
            }
        }

        // 검색 필드
        spec = spec.or(DepartmentSpecification.likeName(departmentGetAllRequest.nameOrDescription()));
        spec = spec.or(DepartmentSpecification.likeDescription(departmentGetAllRequest.nameOrDescription()));

        // Pageable
        Pageable pageable = null;
        int pageSize = departmentGetAllRequest.size() == null ? 10 : departmentGetAllRequest.size();
        switch (sortDirection.toLowerCase()) {
            case "asc" -> pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
            case "desc" -> pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
            default -> pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
        }
        return departmentRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public Department create(Department department) {
        String name = department.getName();
        String description = department.getDescription();

        // 이름, 설명 null 불가
        if (!StringUtils.hasText(name)) {
            throw new BusinessLogicException(ExceptionCode.NAME_CANNOT_BE_NULL);
        }

        if (!StringUtils.hasText(description)) {
            throw new BusinessLogicException(ExceptionCode.DESCRIPTION_CANNOT_BE_NULL);
        }

        // 이름 중복 불가
        if (departmentRepository.existsByName(name)) {
            throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
        }

        // 저장
        return departmentRepository.save(department);
    }

    @Transactional
    @Override
    public Department update(DepartmentUpdateRequest departmentUpdateRequest, Long id) {
        Department department = getDepartment(id);

        // departmentUpdateRequest에 수정할 항목이 비어있으면 기존 값 유지

        String newName = StringUtils.hasText(departmentUpdateRequest.name()) ? departmentUpdateRequest.name() : department.getName();

        String newDescription = StringUtils.hasText(departmentUpdateRequest.description()) ? departmentUpdateRequest.description() : department.getDescription();

        LocalDate newEstablishedDate = (departmentUpdateRequest.establishedDate() != null) ? departmentUpdateRequest.establishedDate() : department.getEstablishedDate();

        // 이름이 중복되면 안됨
        if (!newName.equals(department.getName()) && departmentRepository.existsByName(newName)) {
            throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
        }

        department.update(newName, newDescription, newEstablishedDate);
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    @Override
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Department department = getDepartment(id);
        // 소속 직원이 있는 부서는 삭제 불가
        if (getEmployeeCountByDepartmentId(department.getId()) > 0L) {
            throw new BusinessLogicException(ExceptionCode.DEPARTMENT_HAS_EMPLOYEE_CANNOT_DELETE);
        }
        departmentRepository.delete(department);
    }

    @Override
    public Long getEmployeeCountByDepartmentId(Long departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }

    private LocalDate getCursorDate(String cursorRaw, String sortField, String sortDirection) {
        LocalDate cursorDate = null;

        if ("establishedDate".equals(sortField)) {
            if (StringUtils.hasText(cursorRaw)) {
                try {
                    cursorDate = LocalDate.parse(cursorRaw);
                } catch (DateTimeParseException e) {
                    throw new BusinessLogicException(ExceptionCode.INVALID_DATE_FORMAT);
                }
            }
        }

        if (cursorDate == null) {
            switch (sortDirection) {
                case "asc" -> cursorDate = departmentRepository.findEarliestEstablishedDate();
                case "desc" -> cursorDate = departmentRepository.findLatestEstablishedDate();
                default -> cursorDate = departmentRepository.findEarliestEstablishedDate();
            }
        }

        return cursorDate;
    }
}