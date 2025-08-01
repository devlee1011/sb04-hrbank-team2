package com.codeit.hrbank.backup.entitiy;

import com.codeit.hrbank.base.entity.BaseEntity;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "backups")
public class Backup extends BaseEntity {
    @Column(nullable = false, length = 45)
    private String worker;

    @Column(nullable = false, length = 20)

    @Enumerated(EnumType.STRING)
    private BackupStatus status;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    private Instant endedAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "file_id")
    private StoredFile storedFile;
}
