package com.codeit.hrbank.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        @Schema(description = "내용")
        List<T> content,
        @Schema
        String nextCursor,
        Long nextIdAfter,
        @Schema(type = "integer", format = "int32")
        int size,
        @Schema(type = "integer", format = "int64")
        Long totalElements,
        @Schema(type = "boolean")
        boolean hasNext
) {
    public static <T> PageResponse from(Page<T> page, Long idAfter, String cursor){
        return new PageResponse<> (
                page.getContent(),
                cursor,
                idAfter,
                page.getSize(),
                page.getTotalElements(),
                page.hasNext()
        );
    }
}
