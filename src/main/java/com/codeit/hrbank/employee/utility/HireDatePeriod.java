package com.codeit.hrbank.employee.utility;

import com.codeit.hrbank.employee.entity.UnitType;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import lombok.Getter;

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
                this.to = to != null ? to : LocalDate.now();
                this.from = from != null ? from : this.to.minusDays(12);
            }
            case WEEK -> {
                this.to = to != null ? to : LocalDate.now();
                this.from = from != null ? from : this.to.minusWeeks(12);

            }
            case MONTH -> {
                this.to = to != null ? to.with(TemporalAdjusters.lastDayOfMonth()) : LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                this.from = from != null ? from.with(TemporalAdjusters.lastDayOfMonth()) : this.to.minusMonths(12).with(TemporalAdjusters.lastDayOfMonth());
            }
            case QUARTER -> {
                this.to = to != null ? to.with(TemporalAdjusters.lastDayOfMonth()) : LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                this.from = from != null ? from.with(TemporalAdjusters.lastDayOfMonth()) : this.to.minusMonths(12 * 3).with(TemporalAdjusters.lastDayOfMonth());
            }
            case YEAR -> {
                this.to = to != null ? to.with(TemporalAdjusters.lastDayOfYear()) : LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
                this.from = from != null ? from.with(TemporalAdjusters.lastDayOfYear()) : this.to.minusYears(12).with(TemporalAdjusters.lastDayOfYear());
            }
            default -> {
                throw new BusinessLogicException(ExceptionCode.INVALID_DATE_FORMAT);
            }
        }
    }
}

