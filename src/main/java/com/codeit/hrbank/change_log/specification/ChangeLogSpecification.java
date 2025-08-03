package com.codeit.hrbank.change_log.specification;

import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;

public class ChangeLogSpecification {

    public static Specification<ChangeLog> likeEmployeeNumber(String employeeNumber){
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (employeeNumber == null) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("memo"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + employeeNumber.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<ChangeLog> likeMemo(String memo){
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (memo == null) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("memo"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + memo.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<ChangeLog> likeIpAddress(String ipAddress){
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (ipAddress == null) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("memo"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + ipAddress.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<ChangeLog> betweenAt(Instant startDate, Instant endDate){
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (startDate == null || endDate == null) return null;
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            }
        };
    }

    public static Specification<ChangeLog> equalType(ChangeLogType type) {
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (type == null) return null;
                return criteriaBuilder.equal(root.get("status"), type);
            }
        };
    }

    public static Specification<ChangeLog> greaterThanIpAddress(Long idAfter, String cursor) {
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanIpAddress = criteriaBuilder.greaterThan(root.get("ipAddress"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("ipAddress"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanIpAddress, sort);
            }
        };
    }

    public static Specification<ChangeLog> greaterThanAt(Long idAfter, Instant cursor) {
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanAt = criteriaBuilder.greaterThan(root.get("createdAt"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("createAt"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanAt, sort);
            }
        };
    }

    public static Specification<ChangeLog> lessThanIpAddress(Long idAfter, String cursor) {
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate lessThanIpAddress = criteriaBuilder.lessThan(root.get("ipAddress"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("ipAddress"), cursor),
                        criteriaBuilder.lessThan(root.get("id"), idAfter));
                return criteriaBuilder.or(lessThanIpAddress, sort);
            }
        };
    }

    public static Specification<ChangeLog> lessThanAt(Long idAfter, Instant cursor) {
        return new Specification<ChangeLog>() {
            @Override
            public Predicate toPredicate(Root<ChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate lessThanAt = criteriaBuilder.lessThan(root.get("createAt"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("createAt"), cursor),
                        criteriaBuilder.lessThan(root.get("id"), idAfter));
                return criteriaBuilder.or(lessThanAt, sort);
            }
        };
    }
}
