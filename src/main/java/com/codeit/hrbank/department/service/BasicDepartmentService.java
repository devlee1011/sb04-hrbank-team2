package com.codeit.hrbank.department.service;

import com.codeit.hrbank.department.dto.request.DepartmentGetAllRequest;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Page<Department> getAllDepartments(DepartmentGetAllRequest departmentGetAllRequest) {
        String sortField = departmentGetAllRequest.sortField() == null ? "name" : departmentGetAllRequest.sortField();
        Specification<Department> spec = Specification.unrestricted();

        // 페이지네이션 필터
        if ("name".equals(sortField)) {
            spec = spec.and(departmentGetAllRequest.idAfter(), departmentGetAllRequest.cursor());
        }
        return departmentRepository.findAllWithEmployeeCount();

    }
}


//public Page<Employee> getAll(EmployeeGetAllRequest employeeGetAllRequest) {
//    String sortField = employeeGetAllRequest.sortField() == null ? "name" : employeeGetAllRequest.sortField();
//    Specification<Employee> spec = Specification.unrestricted();
//    if ("name".equals(sortField)) {
//        spec = spec.and(EmployeeSpecification.greaterThanName(employeeGetAllRequest.idAfter(),employeeGetAllRequest.cursor()));
//    } else if ("hireDate".equals(sortField)) {
//        spec = spec.and(EmployeeSpecification.greaterThanHireDate(employeeGetAllRequest.idAfter(), LocalDate.parse(employeeGetAllRequest.cursor())));
//    } else if ("employeeNumber".equals(sortField)) {
//        spec = spec.and(EmployeeSpecification.greaterThanEmployeeNumber(employeeGetAllRequest.idAfter(), employeeGetAllRequest.cursor()));
//    }
//    if (employeeGetAllRequest.nameOrEmail() != null && employeeGetAllRequest.nameOrEmail().contains("@")){
//        spec = spec.and(EmployeeSpecification.likeEmail(employeeGetAllRequest.nameOrEmail()));
//    }
//    else {
//        spec = spec.and(EmployeeSpecification.likeName(employeeGetAllRequest.nameOrEmail()));
//    }
//    spec = spec.and(EmployeeSpecification.likeDepartmentName(employeeGetAllRequest.departmentName()));
//    spec = spec.and(EmployeeSpecification.likePosition(employeeGetAllRequest.position()));
//    spec = spec.and(EmployeeSpecification.likeEmployeeNumber(employeeGetAllRequest.employeeNumber()));
//    spec = spec.and(EmployeeSpecification.betweenHireDate(employeeGetAllRequest.hireDateFrom(),employeeGetAllRequest.hireDateTo()));
//    spec = spec.and(EmployeeSpecification.equalStatus(employeeGetAllRequest.status()));
//
//    Pageable pageable = null;
//    int pageSize = employeeGetAllRequest.size() == null ? 10 : employeeGetAllRequest.size();
//    if (employeeGetAllRequest.sortDirection() == null || employeeGetAllRequest.sortDirection().equals("asc")) {
//        pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).ascending());
//    } else {
//        pageable = PageRequest.ofSize(pageSize).withSort(Sort.by(sortField).descending());
//    }
//
//    Page<Employee> findList = employeeRepository.findAll(spec,pageable);
//
//    return findList;
//}
//}