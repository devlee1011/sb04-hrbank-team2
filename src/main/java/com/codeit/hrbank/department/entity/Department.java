package com.codeit.hrbank.department.entity;

import com.codeit.hrbank.base_entity.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department extends BaseUpdatableEntity {
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate establishedDate;

    public void update(String newName, String newDescription, LocalDate newEstablishedDate) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }

        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (newEstablishedDate != null && !newEstablishedDate.equals(this.establishedDate)) {
            this.establishedDate = newEstablishedDate;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            super.setUpdatedAt(Instant.now());
        }
    }
}
