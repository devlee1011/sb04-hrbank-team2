package com.codeit.hrbank.backup.entitiy;

import com.codeit.hrbank.base_entity.BaseEntity;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
