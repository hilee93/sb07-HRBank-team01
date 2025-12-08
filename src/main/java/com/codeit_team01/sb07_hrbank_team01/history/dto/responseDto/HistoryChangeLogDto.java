package com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto;

import com.codeit_team01.sb07_hrbank_team01.history.entity.History;

import java.time.Instant;

// 직원 정보 수정 이력 (목록 조회용)
public record HistoryChangeLogDto(
        Long id,
        String type,
        String employeeNumber,
        String memo,
        String ipAddress,
        Instant at
) {
    public static HistoryChangeLogDto from(History history) {
        return new HistoryChangeLogDto(
                history.getId(),
                history.getType().name(),
                history.getEmployeeNo(),
                history.getMemo(),
                history.getIpAddress(),
                history.getCreatedAt()
        );
    }
}
