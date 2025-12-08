package com.codeit_team01.sb07_hrbank_team01.backup.service;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.dto.response.BackupResponseDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;

public interface BackupService {
  BackupResponseDto createBackup(String worker);

  // 데이터 백업 이력 목록 조회
  PageResponseDto<BackupResponseDto> getBackupPageList(BackupRequestDto backupRequestDto);

  // 최근 백업 정보 조회
  BackupResponseDto getLatestBackup(BackupStatus backupSTatus);


}
