package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "change_logs")
public class ChangeLog extends BaseEntity {

    @Column(name = "type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ChangeLogType type;

    @Column(name = "employee_number", length = 50, nullable = false)
    private String employeeNumber;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Column(name = "ip_address", length = 45, nullable = false)
    private String ipAddress;
}
