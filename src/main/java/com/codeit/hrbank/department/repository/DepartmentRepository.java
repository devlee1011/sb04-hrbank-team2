package com.codeit.hrbank.department.repository;

import com.codeit.hrbank.department.dto.DepartmentProjection;
import com.codeit.hrbank.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    @Query("""
            SELECT d AS department, COUNT(e.id) AS employeeCount
            FROM Department d
            LEFT JOIN d.employees e
            GROUP BY d
            """)
    List<DepartmentProjection> findAllWithEmployeeCount();
}
