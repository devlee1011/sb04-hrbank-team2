package com.codeit.hrbank.change_log.entity;

import jakarta.persistence.*;

@Entity
@Table
public class ChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
