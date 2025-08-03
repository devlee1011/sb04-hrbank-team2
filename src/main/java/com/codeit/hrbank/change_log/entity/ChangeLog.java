package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany(mappedBy = "changeLog", cascade = {CascadeType.ALL}, orphanRemoval = true)
//    private List<ChangeLogDetail> changeLogDetails = new ArrayList<>();

    public ChangeLog(ChangeLogType type, String employeeNumber, String memo, String ipAddress) {
        this.type = type;
        this.employeeNumber = employeeNumber;
        this.memo = memo;
        this.ipAddress = ipAddress;
    }
}
