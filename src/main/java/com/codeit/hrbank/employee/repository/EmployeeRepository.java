package com.codeit.hrbank.employee.repository;

import com.codeit.hrbank.employee.entity.Employee;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findAllByCreatedAtAfter(Instant lastBackupStart);
}
