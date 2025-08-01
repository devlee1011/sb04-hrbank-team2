package com.codeit.hrbank.employee.entity;

import com.codeit.hrbank.base.entity.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseUpdatableEntity {
}
