package com.codeit.hrbank.employee.entity;

import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import org.springframework.util.StringUtils;

public enum UnitType {
    DAY("일"),
    WEEK("주"),
    MONTH("월"),
    QUARTER("분기"),
    YEAR("년");

    private String value;

    UnitType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UnitType parseUnit(String unit) {
        try {
            return StringUtils.hasText(unit) ? UnitType.valueOf(unit.trim().toUpperCase()) : UnitType.MONTH;
        } catch (RuntimeException e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_TIME_UNIT);
        }
    }
}
