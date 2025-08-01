package com.codeit.hrbank.department.dto.request;

public record DepartmentPageRequest(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection
) {
}
