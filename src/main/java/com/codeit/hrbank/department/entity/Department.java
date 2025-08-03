package com.codeit.hrbank.department.entity;

import com.codeit.hrbank.base_entity.BaseUpdatableEntity;
import com.codeit.hrbank.employee.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department extends BaseUpdatableEntity {
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate establishedDate;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
