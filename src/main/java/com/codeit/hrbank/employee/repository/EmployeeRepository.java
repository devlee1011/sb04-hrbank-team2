package com.codeit.hrbank.employee.repository;

import com.codeit.hrbank.employee.entity.Employee;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);

    Long countByDepartmentId(Long departmentId);

    @Query("SELECT COUNT(e.id) " +
            "FROM Employee e " +
            "WHERE e.status = :status AND e.hireDate BETWEEN :fromDate AND :toDate")
    long countByStatusAndHireDateBetween(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);

    @Query("SELECT e.position as groupKey, COUNT(e.id) as count " +
            "FROM Employee e " +
            "WHERE e.status = :status " +
            "GROUP BY e.position")
    List<EmployeeDistributionProjection> countByPositionAndStatusEquals(@Param("status") EmployeeStatus status);

    @Query("SELECT e.department.name as groupKey, COUNT(e.id) as count " +
            "FROM Employee e " +
            "WHERE e.status = :status " +
            "GROUP BY e.department")
    List<EmployeeDistributionProjection> countByDepartmentAndStatusEquals(@Param("status") EmployeeStatus status);

    @Query("SELECT COUNT(e.id)" +
            " FROM Employee e " +
            "WHERE e.hireDate BETWEEN :from AND :to AND e.status IN ('ACTIVE', 'ON_LEAVE')")
    long countByDate(@Param("from")LocalDate from, @Param("to") LocalDate to);

    List<Employee> findAllByCreatedAtAfter(Instant lastBackupStart);
}
