package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.dto.request.ChangeLogGetAllRequest;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.repository.ChangeLogDetailRepository;
import com.codeit.hrbank.change_log.repository.ChangeLogRepository;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.change_log.specification.ChangeLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BasicChangeLogService implements ChangeLogService {
    private final ChangeLogRepository changeLogRepository;
    private final ChangeLogDetailRepository changeLogDetailRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Page<ChangeLog> getAll(ChangeLogGetAllRequest changeLogGetAllRequest) {
        String sortField = changeLogGetAllRequest.sortField() == null ? "at" : changeLogGetAllRequest.sortField();
        String sortDirection = changeLogGetAllRequest.sortDirection() == null ? "desc" : changeLogGetAllRequest.sortDirection();
        Specification<ChangeLog> spec = Specification.unrestricted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            if ("ipAddress".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.greaterThanIpAddress(changeLogGetAllRequest.idAfter(),changeLogGetAllRequest.cursor()));
            } else if ("at".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.greaterThanAt(changeLogGetAllRequest.idAfter(), LocalDate.parse(changeLogGetAllRequest.cursor())));
            }
        } else {
            if ("ipAddress".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.lessThanIpAddress(changeLogGetAllRequest.idAfter(),changeLogGetAllRequest.cursor()));
            } else if ("at".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.lessThanAt(changeLogGetAllRequest.idAfter(), LocalDate.parse(changeLogGetAllRequest.cursor())));
            }
        }
        spec = spec.and(ChangeLogSpecification.likeEmployeeNumber(changeLogGetAllRequest.employeeNumber()));
        spec = spec.and(ChangeLogSpecification.likeMemo(changeLogGetAllRequest.memo()));
        spec = spec.and(ChangeLogSpecification.likeIpAddress(changeLogGetAllRequest.ipAddress()));
        spec = spec.and(ChangeLogSpecification.betweenHireDate(changeLogGetAllRequest.atFrom(),changeLogGetAllRequest.atTo()));
        spec = spec.and(ChangeLogSpecification.equalType(changeLogGetAllRequest.type()));

        Pageable pageable = null;
        int pageSize = changeLogGetAllRequest.size() == null ? 10 : changeLogGetAllRequest.size();
        if (changeLogGetAllRequest.sortDirection() == null || changeLogGetAllRequest.sortDirection().equals("asc")) {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
        } else {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
        }

        Page<ChangeLog> findList = changeLogRepository.findAll(spec,pageable);

        return findList;
    }
}
