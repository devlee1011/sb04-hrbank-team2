package com.codeit.hrbank.employee.repository;

import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.employee.projection.EmployeeCountByDepartmentProjection;
import com.codeit.hrbank.employee.projection.EmployeeDistributionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @EntityGraph(attributePaths = {"department", "profile"})
    @Override
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

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

    @Query("""
             SELECT COUNT(e.id) as count
             FROM Employee e
             WHERE e.hireDate BETWEEN :from AND :to
               AND e.status IN :statuses
            """)
    long countByHireDateBetween(@Param("from") LocalDate from,
                                @Param("to") LocalDate to,
                                @Param("statuses") Collection<EmployeeStatus> statuses);

    List<Employee> findAllByCreatedAtAfter(Instant lastBackupStart);


    @Query("""
                SELECT COUNT(e.id)
                FROM Employee e
                WHERE e.hireDate BETWEEN :from AND :to
            """)
    long countByHireDateInCurrentMonth(@Param("from") LocalDate from,
                                       @Param("to") LocalDate to);

    @Query("""
    SELECT e.department.id AS departmentId,
            COUNT(e.id) AS employeeCount
    FROM Employee e
    WHERE e.department.id IN :departmentIds
    GROUP BY e.department.id
""")
    List<EmployeeCountByDepartmentProjection> countEmployeeCountsByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);
}
