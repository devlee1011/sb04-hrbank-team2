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

  public static CursorPageResponseBackupDto from(List<BackupDto> content, String nextCursor, Long nextIdAfter,
      Long totalElements, boolean hasNext) {
    return new CursorPageResponseBackupDto(content, nextCursor, nextIdAfter, content.size(), totalElements, hasNext);
  }
}
