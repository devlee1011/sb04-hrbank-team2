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

    private final LocalDate fromStart;
    private final LocalDate toEnd;

    public HireDatePeriod(LocalDate fromStart, LocalDate toEnd) {
        this.fromStart = fromStart;
        this.toEnd = toEnd;
    }

    public HireDatePeriod(UnitType unitType, LocalDate from, LocalDate to) {
        // 일: 초기화 없음, 주: 해당 주차 월요일, 월/분기/년: 해당 월의 1일/마지막일
        switch (unitType) {
            case DAY -> {
                this.fromStart = from != null ? from : to.minusDays(12);
                this.toEnd = to;
            }
            case WEEK -> {
                this.fromStart = from != null ? from.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) : to.minusWeeks(12).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                this.toEnd = to;
            }
            case MONTH -> {
                this.fromStart = from != null ? from.withDayOfMonth(1) : to.minusMonths(12).withDayOfMonth(1);
                this.toEnd = to.with(TemporalAdjusters.lastDayOfMonth());
            }
            case QUARTER -> {
                this.fromStart = from != null ? from.withDayOfMonth(1) : to.minusMonths(12 * 3).withDayOfMonth(1);
                this.toEnd = to.with(TemporalAdjusters.lastDayOfMonth());
            }
            case YEAR -> {
                this.fromStart = from != null ? from.with(TemporalAdjusters.firstDayOfYear()) : to.minusYears(12).with(TemporalAdjusters.firstDayOfMonth());
                this.toEnd = to.with(TemporalAdjusters.lastDayOfYear());
            }
            default -> {
                throw new BusinessLogicException(ExceptionCode.INVALID_DATE_FORMAT);
            }
        }
    }
}

