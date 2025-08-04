package com.codeit.hrbank.employee.utility;

import com.codeit.hrbank.employee.entity.UnitType;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Getter
public class HireDatePeriod {

    private final LocalDate from;
    private final LocalDate to;

    public HireDatePeriod(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public HireDatePeriod(UnitType unitType, LocalDate from, LocalDate to) {
        // 일: 초기화 없음, 주: 해당 주차 월요일, 월/분기/년: 해당 월의 1일/마지막일
        switch (unitType) {
            case DAY -> {
                this.from = from != null ? from : to.minusDays(12);
                this.to = to;
            }
            case WEEK -> {
                this.from = from != null ? from.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) : to.minusWeeks(12).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                this.to = to;
            }
            case MONTH -> {
                this.from = from != null ? from.withDayOfMonth(1) : to.minusMonths(12).withDayOfMonth(1);
                this.to = to.with(TemporalAdjusters.lastDayOfMonth());
            }
            case QUARTER -> {
                this.from = from != null ? from.withDayOfMonth(1) : to.minusMonths(12 * 3).withDayOfMonth(1);
                this.to = to.with(TemporalAdjusters.lastDayOfMonth());
            }
            case YEAR -> {
                this.from = from != null ? from.with(TemporalAdjusters.firstDayOfYear()) : to.minusYears(12).with(TemporalAdjusters.firstDayOfMonth());
                this.to = to.with(TemporalAdjusters.lastDayOfYear());
            }
            default -> {
                throw new BusinessLogicException(ExceptionCode.INVALID_DATE_FORMAT);
            }
        }
    }
}

