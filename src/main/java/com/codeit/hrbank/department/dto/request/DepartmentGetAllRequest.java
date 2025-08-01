package com.codeit.hrbank.department.dto.request;

public record DepartmentGetAllRequest(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection
) {
}
