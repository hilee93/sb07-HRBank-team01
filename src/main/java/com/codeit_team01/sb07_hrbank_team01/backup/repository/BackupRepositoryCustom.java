package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface BackupRepositoryCustom {
    Page<Backup> findBackupPage(BackupRequestDto cond);
    Long countTotalElements(String worker, BackupStatus status, Instant startedAt, Instant endedAt);
}
