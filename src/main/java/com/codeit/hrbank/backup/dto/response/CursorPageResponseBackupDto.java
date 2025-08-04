package com.codeit.hrbank.backup.dto.response;

import com.codeit.hrbank.backup.dto.BackupDto;
import java.util.List;

public record CursorPageResponseBackupDto(
    List<BackupDto> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    boolean hasNext
) {
}
