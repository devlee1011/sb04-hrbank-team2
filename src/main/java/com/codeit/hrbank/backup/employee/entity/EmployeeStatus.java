package com.codeit.hrbank.backup.employee.entity;

public enum EmployeeStatus {
    ACTIVE("재직중"),
    ON_LEAVE("휴직중"),
    RESIGNED("퇴사");

    private String value;

    EmployeeStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
