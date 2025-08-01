package com.codeit.hrbank.employee.service;

import com.codeit.hrbank.change_log.dto.EventLogDto;
import com.codeit.hrbank.change_log.entity.ChangeLogType;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import com.codeit.hrbank.employee.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeGetAllRequest;
import com.codeit.hrbank.employee.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.repository.EmployeeRepository;
import com.codeit.hrbank.employee.specification.EmployeeSpecification;
import com.codeit.hrbank.event.EmployeeLogEvent;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        String sortField = employeeGetAllRequest.sortField() == null ? "name" : employeeGetAllRequest.sortField();
        String sortDirection = employeeGetAllRequest.sortDirection() == null ? "asc" : employeeGetAllRequest.sortDirection();
        Specification<Employee> spec = Specification.unrestricted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
            } else if ("employeeNumber".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.greaterThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
            }
        } else {
            if ("name".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
            } else if ("hireDate".equals(sortField)) {
                spec = spec.and(EmployeeSpecification.lessThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
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

        Page<Employee> findList = employeeRepository.findAll(spec,pageable);

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
        List<EventLogDto> logs = createLogForCreate(savedEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.CREATE,employeeCreateRequest.memo(), getClientIp(httpServletRequest), savedEmployee.getEmployeeNumber()));
        return savedEmployee;
    }

    @Transactional
    @Override
    public Employee update(Long id, EmployeeUpdateRequest employeeUpdateRequest, Long newProfileId, HttpServletRequest httpServletRequest) {
        if (employeeUpdateRequest.email() != null){
            isDuplicateEmail(employeeUpdateRequest.email());
        }
        if (employeeUpdateRequest.departmentId() != null){
            validateDepartment(employeeUpdateRequest.departmentId());
        }

        Employee findEmployee = employeeRepository.findById(id).orElse(null);
        List<EventLogDto> logs = new ArrayList<>();
        Optional.ofNullable(employeeUpdateRequest.name())
                .ifPresent(name -> {
                    logs.add(new EventLogDto("name",findEmployee.getName(),name));
                    findEmployee.setName(name);
                });
        Optional.ofNullable(employeeUpdateRequest.email())
                .ifPresent(email -> {
                    logs.add(new EventLogDto("email",findEmployee.getEmail(),email));
                    findEmployee.setEmail(email);
                });

        Optional.ofNullable(employeeUpdateRequest.departmentId())
                .ifPresent(departmentId -> {
                    Department findDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId()).orElse(null);
                    logs.add(new EventLogDto("DepartmentName", findEmployee.getDepartment().getName(),findDepartment.getName()));
                    findEmployee.setDepartment(findDepartment);
                });

        Optional.ofNullable(employeeUpdateRequest.position())
                .ifPresent(position -> {
                    logs.add(new EventLogDto("position",findEmployee.getPosition(),position));
                    findEmployee.setPosition(position);
                });
        Optional.ofNullable(employeeUpdateRequest.hireDate())
                .ifPresent(hireDate -> {
                    logs.add(new EventLogDto("hireDate",String.valueOf(findEmployee.getHireDate()),String.valueOf(hireDate)));
                    findEmployee.setHireDate(hireDate);
                });
        Optional.ofNullable(employeeUpdateRequest.status())
                .ifPresent(status -> {
                    logs.add(new EventLogDto("status",String.valueOf(findEmployee.getStatus()),String.valueOf(status)));
                    findEmployee.setStatus(status);
                });

        Optional.ofNullable(newProfileId).ifPresent(storedFileId -> {  // 변경할 프로필이 있으면 삭제 후 등록
            Optional.ofNullable(findEmployee.getProfile()).ifPresent(storedFileRepository::delete);
            StoredFile profile = storedFileRepository.findById(newProfileId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORED_FILE_NOT_FOUND));
            findEmployee.setProfile(profile);
        });
        Employee employee = employeeRepository.save(findEmployee);
        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.UPDATE, employeeUpdateRequest.memo(), getClientIp(httpServletRequest), employee.getEmployeeNumber()));
        return employee;
    }

    @Transactional
    @Override
    public void delete(Long id,HttpServletRequest httpServletRequest) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        List<EventLogDto> logs = createLogForDelete(employee);
        employeeRepository.deleteById(id);

        eventPublisher.publishEvent(new EmployeeLogEvent(logs, ChangeLogType.DELETE, "직원삭제", getClientIp(httpServletRequest), employee.getEmployeeNumber()));
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

        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    private List<EventLogDto> createLogForCreate(Employee employee) {
        List<EventLogDto> logs = new ArrayList<>();
        logs.add(new EventLogDto("hireDate","-",String.valueOf(employee.getHireDate())));
        logs.add(new EventLogDto("name","-",employee.getName()));
        logs.add(new EventLogDto("position","-",employee.getPosition()));
        logs.add(new EventLogDto("departmentName","-",employee.getDepartment().getName()));
        logs.add(new EventLogDto("email","-",employee.getEmail()));
        logs.add(new EventLogDto("status" ,"-",employee.getStatus().toString()));
        logs.add(new EventLogDto("employeeNumber","-",employee.getEmployeeNumber()));
        return logs;
    }

    private List<EventLogDto> createLogForDelete(Employee employee) {
        List<EventLogDto> logs = new ArrayList<>();
        logs.add(new EventLogDto("hireDate",String.valueOf(employee.getHireDate()),"-"));
        logs.add(new EventLogDto("name",employee.getName(),"-"));
        logs.add(new EventLogDto("position",employee.getPosition(),"-"));
        logs.add(new EventLogDto("departmentName",employee.getDepartment().getName(),"-"));
        logs.add(new EventLogDto("email",employee.getEmail(),"-"));
        logs.add(new EventLogDto("status",employee.getStatus().toString() ,"-"));
        return logs;
    }
}
