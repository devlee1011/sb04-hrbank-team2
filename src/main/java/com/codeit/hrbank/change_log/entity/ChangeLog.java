package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base_entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "change_logs")
public class ChangeLog extends BaseEntity {
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ChangeLogType type;

    @Column(nullable = false, length = 50)
    private String employeeNumber;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false, length = 45)
    private String ipAddress;

    public ChangeLog(ChangeLogType type, String employeeNumber, String memo, String ipAddress) {
        this.type = type;
        this.employeeNumber = employeeNumber;
        this.memo = memo;
        this.ipAddress = ipAddress;
    }
}
