package com.codeit_team01.sb07_hrbank_team01.backup.dto.response;

import java.time.Instant;

public record BackupResponseDto(
    Long id,
    String worker,
    Instant startedAt,
    Instant endedAt,
    String status,
    Long fileId
) {
}
