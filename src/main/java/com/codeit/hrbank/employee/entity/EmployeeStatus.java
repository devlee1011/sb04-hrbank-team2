package com.codeit.hrbank.employee.entity;

import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import org.springframework.util.StringUtils;

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

    public static EmployeeStatus parseStatus(String status) {
        try {
            return StringUtils.hasText(status) ? EmployeeStatus.valueOf(status.trim().toUpperCase()) : null;
        } catch (RuntimeException e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_EMPLOYEE_STATUS);
        }
    }
}
