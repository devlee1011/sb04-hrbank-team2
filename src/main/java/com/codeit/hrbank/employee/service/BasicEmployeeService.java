package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.employee.specification.EmployeeSpecification;
import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StoredFileRepository storedFileRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest) {
        String sortField = employeeGetAllRequest.sortField() == null ? "name" : employeeGetAllRequest.sortField();
        String sortDirection = employeeGetAllRequest.sortDirection() == null ? "asc" : employeeGetAllRequest.sortDirection();
        Specification<Employee> spec = Specification.unrestricted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanName(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
            } else if ("employeeNumber".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            }
        } else {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanName(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
            } else if ("employeeNumber".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            }
        }
        if (employeeGetAllRequest.nameOrEmail() != null && employeeGetAllRequest.nameOrEmail().contains("@")) {
            spec = spec.and(EmployeeSpecification.likeEmail(employeeGetAllRequest.nameOrEmail()));
        } else {
            spec = spec.and(EmployeeSpecification.likeName(employeeGetAllRequest.nameOrEmail()));
        }
        spec = spec.and(EmployeeSpecification.likeDepartmentName(employeeGetAllRequest.departmentName()));
        spec = spec.and(EmployeeSpecification.likePosition(employeeGetAllRequest.position()));
        spec = spec.and(EmployeeSpecification.likeEmployeeNumber(employeeGetAllRequest.employeeNumber()));
        spec = spec.and(EmployeeSpecification.betweenHireDate(employeeGetAllRequest.hireDateFrom(), employeeGetAllRequest.hireDateTo()));
        spec = spec.and(EmployeeSpecification.equalStatus(employeeGetAllRequest.status()));

        Pageable pageable = null;
        int pageSize = employeeGetAllRequest.size() == null ? 10 : employeeGetAllRequest.size();
        if (employeeGetAllRequest.sortDirection() == null || employeeGetAllRequest.sortDirection().equals("asc")) {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
        } else {
            pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
        }

        Page<Employee> findList = employeeRepository.findAll(spec, pageable);

        return findList;
    }

    @Transactional(readOnly = true)
    @Override
    public Employee getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        return employee;
    }

    @Transactional
    @Override
    public Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId) {
        isDuplicateEmail(employeeCreateRequest.email());

        if (profileId == null) {
            System.out.println("이미지가 포함되지 않아 기본 프로필로 설정됩니다.");
        }
        StoredFile profile = Optional.ofNullable(profileId)
                .flatMap(storedFileRepository::findById)
                .orElse(null);
        Department department = departmentRepository.findById(employeeCreateRequest.departmentId()).orElse(null);
        Employee employee = new Employee(
                employeeCreateRequest.name(), employeeCreateRequest.email(),
                department, employeeCreateRequest.position(),
                employeeCreateRequest.hireDate(), profile
        );
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setEmployeeNumber(
                String.format("EMP-%d-%03d",
                        savedEmployee.getHireDate().getYear(),
                        savedEmployee.getId())
        );
        employeeRepository.save(savedEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(employee, ChangeLogType.CREATE, employeeCreateRequest.memo()));
        return savedEmployee;
    }

    @Transactional
    @Override
    public Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long newProfileId) {
        if (employeeUpdateRequest.email() != null) {
            isDuplicateEmail(employeeUpdateRequest.email());
        }
        if (employeeUpdateRequest.departmentId() != null) {
            validateDepartment(employeeUpdateRequest.departmentId());
        }

        Employee findEmployee = employeeRepository.findById(id).orElse(null);

        Optional.ofNullable(employeeUpdateRequest.name())
                .ifPresent(findEmployee::setName);
        Optional.ofNullable(employeeUpdateRequest.email())
                .ifPresent(findEmployee::setEmail);

        Optional.ofNullable(employeeUpdateRequest.departmentId())
                .ifPresent(departmentId -> {
                    Department findDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId()).orElse(null);
                    findEmployee.setDepartment(findDepartment);
                });

        Optional.ofNullable(employeeUpdateRequest.position())
                .ifPresent(findEmployee::setPosition);
        Optional.ofNullable(employeeUpdateRequest.hireDate())
                .ifPresent(findEmployee::setHireDate);
        Optional.ofNullable(employeeUpdateRequest.status())
                .ifPresent(findEmployee::setStatus);

        Optional.ofNullable(newProfileId).ifPresent(storedFileId -> {  // 변경할 프로필이 있으면 삭제 후 등록
            Optional.ofNullable(findEmployee.getProfile()).ifPresent(storedFileRepository::delete);
            StoredFile profile = storedFileRepository.findById(newProfileId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORED_FILE_NOT_FOUND));
            findEmployee.setProfile(profile);
        });

        Employee employee = employeeRepository.save(findEmployee);
        String department = Optional.ofNullable(employeeUpdateRequest.departmentId())
                .flatMap(departmentRepository::findById)
                .map(Department::getName)
                .orElse(null);
        eventPublisher.publishEvent(new EmployeeLogEvent(employeeUpdateRequest, department));
        return employee;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        employeeRepository.deleteById(id);

        eventPublisher.publishEvent(new EmployeeLogEvent(employee, ChangeLogType.DELETE, "직원삭제"));
    }

    @Transactional(readOnly = true)
    @Override
    public long getCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) fromDate = LocalDate.of(1900, 1, 1);
        if (toDate == null) toDate = LocalDate.now();
        return employeeRepository.countByStatusAndHireDateBetween(status, fromDate, toDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDistributionProjection> getDistribution(String groupBy, EmployeeStatus status) {
        return switch (groupBy) {
            case "department" -> employeeRepository.countByDepartmentAndStatusEquals(status);
            case "position" -> employeeRepository.countByPositionAndStatusEquals(status);
            default -> employeeRepository.countByDepartmentAndStatusEquals(status);
        };
    }

    @Transactional(readOnly = true)
    @Override
    public Map<LocalDate, Long> getTrend(LocalDate from, LocalDate to, String unit) {
        if (!StringUtils.hasText(unit)) unit = "month";
        if (to == null) to = LocalDate.now();

        Map<LocalDate, Long> result = new LinkedHashMap<>();
        switch (unit) {
            case "month" -> {
                if (from == null) from = to.minusMonths(12).withDayOfMonth(1);
                else from = from.withDayOfMonth(1); // 정직하게 해당 월의 시작으로 맞춤
                to = to.withDayOfMonth(1); // to도 월 단위 비교를 위해 시작일로 맞춤

                long monthsBetween = ChronoUnit.MONTHS.between(from, to);
                for (int i = 0; i <= monthsBetween; i++) {
                    LocalDate monthEnd = from.plusMonths(i);
                    monthEnd = monthEnd.withDayOfMonth(monthEnd.lengthOfMonth());

                    long count = employeeRepository.countByDate(from, monthEnd);
                    result.put(monthEnd, count);
                }
            }
            case "day" -> {
                if (from == null) from = to.minusDays(12);
                long daysBetween = ChronoUnit.DAYS.between(from, to);
                for (int i = 0; i <= daysBetween; i++) {
                    LocalDate dayEnd = from.plusDays(i);
                    long count = employeeRepository.countByDate(from, dayEnd);
                    result.put(dayEnd, count);
                }
            }
            case "week" -> {
                if (from == null) from = to.minusWeeks(12);
                long weeksBetween = ChronoUnit.WEEKS.between(from, to);
                for (int i = 0; i <= weeksBetween; i++) {
                    LocalDate weekEnd = from.plusWeeks(i);
                    long count = employeeRepository.countByDate(from, weekEnd);
                    result.put(weekEnd, count);
                }
            }
            case "year" -> {
                if (from == null) from = to.minusYears(12);
                long yearsBetween = ChronoUnit.YEARS.between(from, to);
                for (int i = 0; i <= yearsBetween; i++) {
                    LocalDate yearEnd = from.plusYears(i);
                    long count = employeeRepository.countByDate(from, yearEnd);
                    result.put(yearEnd, count);
                }
            }
            case "quarter" -> {
                if (from == null) from = to.minusMonths(12*3);
                long quartersBetween = ChronoUnit.MONTHS.between(from, to);
                for (int i = 0; i <= quartersBetween; i = i + 3) {
                    LocalDate quarterEnd = from.plusMonths(i);
                    long count = employeeRepository.countByDate(from, quarterEnd);
                    result.put(quarterEnd, count);
                }
            }
        }
        return result;
    }

    private void isDuplicateEmail(String email) {
        if (employeeRepository.existsByEmail(email))
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
    }

    private void validateDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND);
    }

    private void validateEmployee(Long id) {
        if (!employeeRepository.existsById(id)) throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND);
    }
}
