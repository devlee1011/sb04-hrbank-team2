package com.codeit.hrbank.stored_file.entity;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class StoredFile extends BaseEntity {
}
