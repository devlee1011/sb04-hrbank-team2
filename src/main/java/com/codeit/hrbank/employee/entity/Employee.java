package com.codeit.hrbank.employee.entity;

import com.codeit.hrbank.department.entity.Department;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    String position;
    @Column(nullable = false)
    LocalDate hireDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    EmployeeStatus status;
    @Column(nullable = false)
    String employee_number;
    @Column(nullable = false)
    @CreatedDate
    Instant createdAt;
    @Column
    @LastModifiedDate
    Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    File profile
}
