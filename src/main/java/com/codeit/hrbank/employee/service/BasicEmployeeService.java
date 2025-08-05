package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.employee.entity.UnitType;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import com.codeit.hrbank.employee.projection.EmployeeTrendProjection;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.employee.specification.EmployeeSpecification;
import com.codeit.hrbank.employee.utility.HireDatePeriod;
import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StoredFileRepository storedFileRepository;

    @Override
    public Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest) {
        String sortField = StringUtils.hasText(employeeGetAllRequest.sortField()) ? employeeGetAllRequest.sortField() : "name";
        String sortDirection = StringUtils.hasText(employeeGetAllRequest.sortDirection()) ? employeeGetAllRequest.sortDirection() : "asc";
        Specification<Employee> spec = Specification.unrestricted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanHireDate(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            } else if ("employeeNumber".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            }
        } else {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanHireDate(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            } else if ("employeeNumber".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            }
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

        Page<Employee> findList = employeeRepository.findAll(spec, pageable);

        return findList;
    }

    @Override
    public Employee getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        return employee;
    }

    @Transactional
    @Override
    public Employee create(EmployeeCreateRequest employeeCreateRequest, Long profileId, HttpServletRequest httpServletRequest) {
        isDuplicateEmail(employeeCreateRequest.email());

        if (profileId == null) {
            System.out.println("이미지가 포함되지 않아 기본 프로필로 설정됩니다.");
        }
        StoredFile profile = Optional.ofNullable(profileId)
                .flatMap(storedFileRepository::findById)
                .orElse(null);

        Long departmentId = Optional.ofNullable(employeeCreateRequest.departmentId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_CANNOT_BE_NULL));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));

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
        List<DiffDto> logs = createLogForCreate(savedEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.CREATED, employeeCreateRequest.memo(), getClientIp(httpServletRequest), savedEmployee.getEmployeeNumber()));
        return savedEmployee;
    }

    @Transactional
    @Override
    public Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long newProfileId, HttpServletRequest httpServletRequest) {
        Employee findEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));

        if (employeeUpdateRequest.email() != null && !employeeUpdateRequest.email().equals(findEmployee.getEmail())) {
            isDuplicateEmail(employeeUpdateRequest.email());
        }
        if (employeeUpdateRequest.departmentId() != null){
            validateDepartment(employeeUpdateRequest.departmentId());
        }

        List<DiffDto> logs = new ArrayList<>();

        Optional.ofNullable(employeeUpdateRequest.name())
                .filter(StringUtils::hasText)
                .ifPresent(name -> {
                    logs.add(new DiffDto("name",findEmployee.getName(),name));
                    findEmployee.setName(name);
                });

        Optional.ofNullable(employeeUpdateRequest.email())
                .filter(StringUtils::hasText)
                .ifPresent(email -> {
                    logs.add(new DiffDto("email",findEmployee.getEmail(),email));
                    findEmployee.setEmail(email);
                });

        Optional.ofNullable(employeeUpdateRequest.departmentId())
                .ifPresent(departmentId -> {
                    Department findDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId())
                                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND));
                    logs.add(new DiffDto("department", findEmployee.getDepartment().getName(),findDepartment.getName()));
                    findEmployee.setDepartment(findDepartment);
                });

        Optional.ofNullable(employeeUpdateRequest.position())
                .filter(StringUtils::hasText)
                .ifPresent(position -> {
                    logs.add(new DiffDto("position",findEmployee.getPosition(),position));
                    findEmployee.setPosition(position);
                });

        Optional.ofNullable(employeeUpdateRequest.hireDate())
                .ifPresent(hireDate -> {
                    logs.add(new DiffDto("hireDate",String.valueOf(findEmployee.getHireDate()),String.valueOf(hireDate)));
                    findEmployee.setHireDate(hireDate);
                });

        Optional.ofNullable(employeeUpdateRequest.status())
                .ifPresent(status -> {
                    logs.add(new DiffDto("status",String.valueOf(findEmployee.getStatus()),String.valueOf(status)));
                    findEmployee.setStatus(status);
                });

        Optional.ofNullable(newProfileId).ifPresent(storedFileId -> {  // 변경할 프로필이 있으면 삭제 후 등록
            Optional.ofNullable(findEmployee.getProfile()).ifPresent(storedFileRepository::delete);
            StoredFile profile = storedFileRepository.findById(newProfileId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORED_FILE_NOT_FOUND));
            findEmployee.setProfile(profile);
        });

        Employee employee = employeeRepository.save(findEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.UPDATED, employeeUpdateRequest.memo(), getClientIp(httpServletRequest), employee.getEmployeeNumber()));
        return employee;
    }

    @Transactional
    @Override
    public void delete(Long id,HttpServletRequest httpServletRequest) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        List<DiffDto> logs = createLogForDelete(employee);
        employeeRepository.deleteById(id);
        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.DELETED, "직원삭제", getClientIp(httpServletRequest), employee.getEmployeeNumber()));
    }

    @Transactional(readOnly = true)
    @Override
    public Long getCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) fromDate = LocalDate.of(1900, 1, 1);
        if (toDate == null) toDate = LocalDate.now().plusYears(200);
        if (status == null) return employeeRepository.countTotalEmployee();
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
    public List<EmployeeTrendProjection> getTrend(LocalDate from, LocalDate to, UnitType unit) {
        HireDatePeriod targetPeriod = new HireDatePeriod(unit, from, to);
        var statuses = List.of(EmployeeStatus.ACTIVE, EmployeeStatus.ON_LEAVE);
        return getEmployeeTrendProjections(targetPeriod.getFrom(), targetPeriod.getTo(), statuses, unit);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getEmployeeCountInCurrentMonth() {
        LocalDate from = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate to = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        return employeeRepository.countByHireDateInCurrentMonth(from, to);
    }

    // 응답 dto 생성용 단위 별 날짜 생성기
    private List<EmployeeTrendProjection> getEmployeeTrendProjections(LocalDate from, LocalDate to, Collection<EmployeeStatus> statuses, UnitType unit) {
        List<EmployeeTrendProjection> result = new ArrayList<>();

        long prevCount = 0L;

        switch (unit) {
            case DAY -> {
                for (LocalDate cur = from;
                     !cur.isAfter(to);
                     cur = cur.plusDays(1)) {
                    long currentCount = employeeRepository.countByTargetDate(cur, statuses);
                    result.add(
                            new EmployeeTrendProjection(
                                    cur,
                                    currentCount,
                                    prevCount
                            )
                    );
                    prevCount = currentCount;
                }
            }
            case WEEK -> {
                for (LocalDate cur = from;
                     !cur.isAfter(to);
                     cur = cur.plusWeeks(1)) {
                    long currentCount = employeeRepository.countByTargetDate(cur, statuses);
                    result.add(
                            new EmployeeTrendProjection(
                                    cur,
                                    currentCount,
                                    prevCount
                            )
                    );
                    prevCount = currentCount;
                }
            }
            case MONTH -> {
                for (LocalDate cur = from;
                     !cur.isAfter(to); cur = cur.plusMonths(1)) {
                    long currentCount = employeeRepository.countByTargetDate(cur, statuses);
                    result.add(
                            new EmployeeTrendProjection(
                                    cur.with(TemporalAdjusters.lastDayOfMonth()),
                                    currentCount,
                                    prevCount
                            )
                    );
                    prevCount = currentCount;
                }
            }
            case QUARTER -> {
                for (LocalDate cur = from;
                     !cur.isAfter(to);
                     cur = cur.plusMonths(3)) {
                    long currentCount = employeeRepository.countByTargetDate(cur, statuses);
                    result.add(
                            new EmployeeTrendProjection(
                                    cur.with(TemporalAdjusters.lastDayOfMonth()),
                                    currentCount,
                                    prevCount
                            )
                    );
                    prevCount = currentCount;
                }
            }
            case YEAR -> {
                for (LocalDate cur = from;
                     !cur.isAfter(to);
                     cur = cur.plusYears(1)) {
                    long currentCount = employeeRepository.countByTargetDate(cur, statuses);
                    result.add(
                            new EmployeeTrendProjection(
                                    cur.with(TemporalAdjusters.lastDayOfMonth()),
                                    currentCount,
                                    prevCount
                            )
                    );
                    prevCount = currentCount;
                }
            }
            default -> {
                return List.of();
            }
        }
        return result;
    }

    private void isDuplicateEmail(String email) {
        if(employeeRepository.existsByEmail(email)) throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
    }

    private void validateDepartment(Long departmentId) {
        if(!departmentRepository.existsById(departmentId)) throw new BusinessLogicException(ExceptionCode.DEPARTMENT_ID_IS_NOT_FOUND);
    }

    private void validateEmployee(Long id) {
        if(!employeeRepository.existsById(id)) throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // IPv6 로컬 loopback → IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    private List<DiffDto> createLogForCreate(Employee employee) {
        List<DiffDto> logs = new ArrayList<>();
        logs.add(new DiffDto("hireDate","-",String.valueOf(employee.getHireDate())));
        logs.add(new DiffDto("name","-",employee.getName()));
        logs.add(new DiffDto("position","-",employee.getPosition()));
        logs.add(new DiffDto("department","-",employee.getDepartment().getName()));
        logs.add(new DiffDto("email","-",employee.getEmail()));
        logs.add(new DiffDto("status" ,"-",employee.getStatus().toString()));
        logs.add(new DiffDto("employeeNumber","-",employee.getEmployeeNumber()));
        return logs;
    }

    private List<DiffDto> createLogForDelete(Employee employee) {
        List<DiffDto> logs = new ArrayList<>();
        logs.add(new DiffDto("hireDate",String.valueOf(employee.getHireDate()),"-"));
        logs.add(new DiffDto("name",employee.getName(),"-"));
        logs.add(new DiffDto("position",employee.getPosition(),"-"));
        logs.add(new DiffDto("department",employee.getDepartment().getName(),"-"));
        logs.add(new DiffDto("email",employee.getEmail(),"-"));
        logs.add(new DiffDto("status",employee.getStatus().toString() ,"-"));
        return logs;
    }
}
