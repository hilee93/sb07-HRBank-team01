package com.codeit_team01.sb07_hrbank_team01.backup.entity;

import lombok.Getter;

@Getter
public enum BackupStatus {
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  FAILED("실패"),
  SKIPPED("건너뜀");

  private final String progress;

  BackupStatus(String progress) {
    this.progress = progress;
  }
}
