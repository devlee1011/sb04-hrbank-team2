package com.codeit.hrbank.backup.employee.specification;

import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.employee.entity.EmployeeStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecification {

    public static Specification<Employee> likeName(String name){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (name == null) return null;
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }

    public static Specification<Employee> likeEmail(String email){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (email == null) return null;
                return criteriaBuilder.like(root.get("email"), "%" + email + "%");
            }
        };
    }

    public static Specification<Employee> likeDepartmentName(String departmentName){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (departmentName == null) return null;
                return criteriaBuilder.like(root.get("department").get("name"), "%" + departmentName + "%");
            }
        };
    }

    public static Specification<Employee> likePosition(String position){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (position == null) return null;
                return criteriaBuilder.like(root.get("position"), "%" + position + "%");
            }
        };
    }

    public static Specification<Employee> likeEmployeeNumber(String employeeNumber){
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (employeeNumber == null) return null;
                return criteriaBuilder.like(root.get("employeeNumber"), "%" + employeeNumber + "%");
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
