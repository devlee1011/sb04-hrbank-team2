package com.codeit.hrbank.department.specification;

import com.codeit.hrbank.department.entity.Department;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    // 부분 조건: 부서명
    public static Specification<Department> likeName(String name) {
        return new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                if (name == null) return null;
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }

    // 부분 조건: 부서 설명
    public static Specification<Department> likeDescription(String description) {
        return new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                if (description == null) return null;
                return criteriaBuilder.like(root.get("description"), "%" + description + "%");
            }
        };
    }

    // ASC 정렬 조건: 부서명
    public static Specification<Department> greaterThanName(Long idAfter, String cursor) {
        return new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root,
                                        CriteriaQuery<?> query,
                                        CriteriaBuilder criteriaBuilder) {
                if (idAfter == null || cursor == null) return null;

                Predicate greaterThanName = criteriaBuilder.greaterThan(root.get("name"), cursor);
                Predicate sort = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("name"), cursor),
                        criteriaBuilder.greaterThan(root.get("id"), idAfter)
                );
                return criteriaBuilder.or(greaterThanName, sort);
            }
        };
    }
}
