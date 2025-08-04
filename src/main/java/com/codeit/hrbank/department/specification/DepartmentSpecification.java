package com.codeit.hrbank.department.specification;

import com.codeit.hrbank.department.entity.Department;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class DepartmentSpecification {

    // 부분 일치 조건: 이름
    public static Specification<Department> likeName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(name)) return null;
            return criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("name"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                    "%" + name.trim().toLowerCase().replaceAll("\\s+", "") + "%");
        };
    }

    // 부분 일치 조건: 설명
    public static Specification<Department> likeDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(description)) return null;
            return criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("description"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                    "%" + description.trim().toLowerCase().replaceAll("\\s+", "") + "%");
        };
    }

    // ASC 정렬 조건: 이름
    public static Specification<Department> greaterThanName(Long idAfter, String cursor) {
        return (root, query, criteriaBuilder) -> {
            if (idAfter == null || cursor == null) return null;

            Predicate greaterThanName = criteriaBuilder.greaterThan(root.get("name"), cursor);
            Predicate sort = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("name"), cursor),
                    criteriaBuilder.greaterThan(root.get("id"), idAfter)
            );
            return criteriaBuilder.or(greaterThanName, sort);
        };
    }
    
    // ASC 정렬 조건: 설립일
    public static Specification<Department> greaterThanEstablishedDate(Long idAfter, LocalDate cursor) {
        return (root, query, criteriaBuilder) -> {
            if (idAfter == null || cursor == null) return null;
            Predicate greaterThanEstablishedDate = criteriaBuilder.greaterThan(root.get("name"), cursor);
            Predicate sort = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("establishedDate"), cursor),
                    criteriaBuilder.greaterThan(root.get("id"), idAfter)
            );
            return criteriaBuilder.or(greaterThanEstablishedDate, sort);
        };
    }

    // DESC 정렬 조건: 이름
    public static Specification<Department> lessThanName(Long idAfter, String cursor) {
        return (root, query, criteriaBuilder) -> {
            if (idAfter == null || cursor == null) return null;
            Predicate lessThanName = criteriaBuilder.lessThan(root.get("name"), cursor);
            Predicate sort = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("name"), cursor),
                    criteriaBuilder.greaterThan(root.get("id"), idAfter)
            );
            return criteriaBuilder.or(lessThanName, sort);
        };
    }

    // DESC 정렬 조건: 설립일
    public static Specification<Department> lessThanEstablishedDate(Long idAfter, LocalDate cursor) {
        return (root, query, criteriaBuilder) -> {
            if (idAfter == null || cursor == null) return null;
            Predicate lessThanEstablishedDate = criteriaBuilder.lessThan(root.get("name"), cursor);
            Predicate sort = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("establishedDate"), cursor),
                    criteriaBuilder.lessThan(root.get("id"), idAfter)
            );
            return criteriaBuilder.or(lessThanEstablishedDate, sort);
        };
    }

}
