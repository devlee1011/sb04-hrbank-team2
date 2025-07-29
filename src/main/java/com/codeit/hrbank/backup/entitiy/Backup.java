package com.codeit.hrbank.backup.entitiy;

import jakarta.persistence.*;

@Entity
@Table
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
