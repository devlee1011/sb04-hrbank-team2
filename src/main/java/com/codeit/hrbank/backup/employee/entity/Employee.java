package com.codeit.hrbank.backup.employee.entity;

import com.codeit.hrbank.base.entity.BaseUpdatableEntity;
import com.codeit.hrbank.department.entity.Department;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseUpdatableEntity {
    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(nullable = false, length = 20)
    String position;

    @Column(nullable = false)
    LocalDate hireDate;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    EmployeeStatus status;

    @Column(length = 50)
    String employeeNumber;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    StoredFile profile;

    public Employee(String name, String email, Department department, String position, LocalDate hireDate, StoredFile profile) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
        this.hireDate = hireDate;
        this.profile = profile;
        this.status = EmployeeStatus.ACTIVE;
    }
}
