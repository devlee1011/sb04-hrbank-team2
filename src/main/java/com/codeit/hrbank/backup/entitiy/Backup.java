package com.codeit.hrbank.backup.entitiy;

import com.codeit.hrbank.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "backups")
public class Backup extends BaseEntity {
}
