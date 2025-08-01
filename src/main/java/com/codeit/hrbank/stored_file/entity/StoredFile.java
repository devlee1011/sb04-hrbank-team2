package com.codeit.hrbank.stored_file.entity;

import com.codeit.hrbank.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class StoredFile extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String fileName;

  @Column(nullable = false, length = 50)
  private String type;

  @Column(nullable = false)
  private Long size;

  public StoredFile(MultipartFile file) {
    this.fileName = file.getOriginalFilename();
    this.type = file.getContentType();
    this.size = file.getSize();
  }
}
