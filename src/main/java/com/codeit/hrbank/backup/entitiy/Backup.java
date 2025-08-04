package com.codeit.hrbank.backup.entitiy;

import com.codeit.hrbank.base_entity.BaseEntity;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "backups")
public class Backup extends BaseEntity {

  @Column(nullable = false, length = 45)
  private String worker;

  @Column(nullable = false, length = 20)
  
  @Enumerated(EnumType.STRING)
  private BackupStatus status;

  @Column
  private Instant startedAt;

  @Column
  private Instant endedAt;

  @OneToOne
  @JoinColumn(name = "file_id")
  private StoredFile storedFile;
}
