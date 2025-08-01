package com.codeit.hrbank.change_log.entity;

import com.codeit.hrbank.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "change_logs")
public class ChangeLog extends BaseEntity {
}
