package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_change_details")
public class ChangeLogDetail extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "log_id")
    ChangeLog changeLog;

    @Column(nullable = false, length = 100)
    String fieldName;

    @Column
    String oldValue;

    @Column
    String newValue;
}
