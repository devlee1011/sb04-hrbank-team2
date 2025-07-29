package com.codeit.hrbank.stored_file.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class StoredFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
