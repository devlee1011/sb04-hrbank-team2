package com.codeit.hrbank.department.repository;

import com.codeit.hrbank.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;


public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    boolean existsByName(String name);

    @Query("SELECT MIN(d.establishedDate) FROM Department d")
    LocalDate findEarliestEstablishedDate();

    @Query("SELECT MAX(d.establishedDate) FROM Department d")
    LocalDate findLatestEstablishedDate();
}
