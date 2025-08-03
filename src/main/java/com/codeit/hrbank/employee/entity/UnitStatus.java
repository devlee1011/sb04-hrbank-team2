package com.codeit.hrbank.employee.entity;

public enum UnitStatus {
    DAY("일"),
    WEEK("주"),
    MONTH("월"),
    QUARTER("분기"),
    YEAR("년");

    private String value;

    UnitStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
