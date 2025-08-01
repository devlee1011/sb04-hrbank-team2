package com.codeit.hrbank.backup.entitiy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BackupStatus {
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  FAILED("실패"),
  SKIPPED("건너뜀");

  private String value;
}
