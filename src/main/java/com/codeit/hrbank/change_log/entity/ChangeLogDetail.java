package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee_change_details")
public class ChangeLogDetail extends BaseEntity {
}
