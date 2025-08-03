package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import com.codeit.hrbank.change_log.dto.request.ChangeLogGetAllRequest;
import com.codeit.hrbank.change_log.repository.ChangeLogDetailRepository;
import com.codeit.hrbank.change_log.repository.ChangeLogRepository;
import com.codeit.hrbank.change_log.specification.ChangeLogSpecification;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.event.EmployeeLogEvent;
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

import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicChangeLogService implements ChangeLogService {
    private final ChangeLogRepository changeLogRepository;
    private final ChangeLogDetailRepository changeLogDetailRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<ChangeLog> getAll(ChangeLogGetAllRequest changeLogGetAllRequest) {
        String sortField = changeLogGetAllRequest.sortField() == null ? "at" : changeLogGetAllRequest.sortField();
        if (sortField.equals("at")) sortField = "createdAt";
        String sortDirection = changeLogGetAllRequest.sortDirection() == null ? "desc" : changeLogGetAllRequest.sortDirection();
        Specification<ChangeLog> spec = Specification.unrestricted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            if ("ipAddress".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.greaterThanIpAddress(changeLogGetAllRequest.idAfter(),changeLogGetAllRequest.cursor()));
            } else if ("createdAt".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.greaterThanAt(changeLogGetAllRequest.idAfter(), Instant.parse(Optional.ofNullable(changeLogGetAllRequest.cursor()).orElse(String.valueOf(Instant.MIN)))));
            }
        } else {
            if ("ipAddress".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.lessThanIpAddress(changeLogGetAllRequest.idAfter(),changeLogGetAllRequest.cursor()));
            } else if ("createdAt".equals(sortField)) {
                spec = spec.and(ChangeLogSpecification.lessThanAt(changeLogGetAllRequest.idAfter(), Instant.parse(Optional.ofNullable(changeLogGetAllRequest.cursor()).orElse(String.valueOf(Instant.now())))));
            }
        }
        spec = spec.and(ChangeLogSpecification.likeEmployeeNumber(changeLogGetAllRequest.employeeNumber()));
        spec = spec.and(ChangeLogSpecification.likeMemo(changeLogGetAllRequest.memo()));
        spec = spec.and(ChangeLogSpecification.likeIpAddress(changeLogGetAllRequest.ipAddress()));
        spec = spec.and(ChangeLogSpecification.betweenAt(changeLogGetAllRequest.atFrom(),changeLogGetAllRequest.atTo()));
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

    @Transactional(readOnly = true)
    @Override
    public List<ChangeLogDetail> getChangeLogDetail(Long id) {
        List<ChangeLogDetail> detailLogs = changeLogDetailRepository.findAllByChangeLogId(id);
        if (detailLogs.isEmpty()) throw new BusinessLogicException(ExceptionCode.LOG_NOT_FOUND);
        return detailLogs;
    }

    @Transactional
    @Override
    public void create(EmployeeLogEvent event) {
        ChangeLog changeLog = new ChangeLog(event.getLogStatus(),event.getEmployeeNumber(), event.getMemo(), event.getIpAddress());
        ChangeLog savedChangeLog = changeLogRepository.save(changeLog);
        changeLogRepository.flush();
        List<ChangeLogDetail> details = createDetailLogs(event,savedChangeLog);
        changeLogDetailRepository.saveAll(details);
    }

    private List<ChangeLogDetail> createDetailLogs(EmployeeLogEvent event, ChangeLog changeLog) {
        List<ChangeLogDetail> details = new ArrayList<>();
        for (DiffDto c : event.getChangelogs()) {
            if (!Objects.equals(c.before(), c.after())) {
                details.add(new ChangeLogDetail(changeLog, c.propertyName(),c.before(),c.after()));
            }
        }
        return details;
    }

    @Transactional(readOnly = true)
    @Override
    public Long getCount(Instant fromDate, Instant toDate) {
        return changeLogRepository.countByCreatedAtBetween(fromDate,toDate);
    }
}
