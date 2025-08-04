package com.codeit.hrbank.change_log.entity;

import lombok.Getter;

@Getter
public enum ChangeLogType {
    CREATE("추가"),
    UPDATE("수정"),
    DELETE("삭제"),
    CREATED("생성"),
    UPDATED("수정"),
    DELETED("삭제");

    private final String description;

    ChangeLogType(String description) {
        this.description = description;
    }
}