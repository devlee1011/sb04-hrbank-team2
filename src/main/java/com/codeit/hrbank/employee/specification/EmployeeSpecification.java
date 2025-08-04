package com.codeit.hrbank.employee.specification;

import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class EmployeeSpecification {

    public static Specification<Employee> likeName(String name){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.hasText(name)) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("name"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + name.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<Employee> likeEmail(String email){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.hasText(email)) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("email"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + email.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<Employee> likeDepartmentName(String departmentName){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.hasText(departmentName)) return null;

                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("department").get("name"), criteriaBuilder.literal((" ")), criteriaBuilder.literal(""))),
                        "%" + departmentName.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<Employee> likePosition(String position){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.hasText(position)) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("position"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + position.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<Employee> likeEmployeeNumber(String employeeNumber){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.hasText(employeeNumber)) return null;
                return criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("replace", String.class, root.get("employeeNumber"), criteriaBuilder.literal(" "), criteriaBuilder.literal(""))),
                        "%" + employeeNumber.trim().toLowerCase().replaceAll("\\s+", "") + "%");
            }
        };
    }

    public static Specification<Employee> betweenHireDate(LocalDate startDate, LocalDate endDate){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (startDate == null || endDate == null) return null;
                return criteriaBuilder.between(root.get("hireDate"), startDate, endDate);
            }
        };
    }

    public static Specification<Employee> equalStatus(EmployeeStatus status) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (status == null) return null;
                return criteriaBuilder.equal(root.get("status"), status);
            }
        };
    }

    public static Specification<Employee> greaterThanName(Long idAfter, String cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.greaterThan(root.get("name"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("name"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }

    public static Specification<Employee> greaterThanHireDate(Long idAfter, LocalDate cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.greaterThan(root.get("hireDate"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("hireDate"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }

    public static Specification<Employee> greaterThanEmployeeNumber(Long idAfter, String cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.greaterThan(root.get("employeeNumber"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("employeeNumber"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }

    public static Specification<Employee> lessThanName(Long idAfter, String cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.lessThan(root.get("name"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("name"), cursor),
                        criteriaBuilder.lessThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }

    public static Specification<Employee> lessThanHireDate(Long idAfter, LocalDate cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.lessThan(root.get("hireDate"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("hireDate"), cursor),
                        criteriaBuilder.lessThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }

    public static Specification<Employee> lessThanEmployeeNumber(Long idAfter, String cursor) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;
                Predicate greaterThanName = criteriaBuilder.lessThan(root.get("employeeNumber"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("employeeNumber"), cursor),
                        criteriaBuilder.lessThan(root.get("id"), idAfter));
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }
}
