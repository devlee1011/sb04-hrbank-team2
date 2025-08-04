package com.codeit.hrbank.employee.repository;

import com.codeit.hrbank.employee.entity.Employee;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @EntityGraph(attributePaths = {"department","profile"})
    @Override
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    boolean existsByEmail(String email);

    Long countByDepartmentId(Long departmentId);

    List<Employee> findAllByCreatedAtAfter(Instant lastBackupStart);
}
