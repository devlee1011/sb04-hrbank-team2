package com.codeit.hrbank.stored_file.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class StoredFile extends BaseEntity {
  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private Long size;
}
