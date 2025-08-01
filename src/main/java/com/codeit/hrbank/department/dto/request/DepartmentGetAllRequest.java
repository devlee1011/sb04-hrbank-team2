package com.codeit.hrbank.department.dto.request;

public record DepartmentGetAllRequest(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}
