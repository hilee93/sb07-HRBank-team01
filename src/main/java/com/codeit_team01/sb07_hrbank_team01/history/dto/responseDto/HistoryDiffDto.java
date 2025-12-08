package com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto;

import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryDetail;

// 직원 정보 수정 이력 변경 내용 (상세 조회용)
public record HistoryDiffDto(
        String propertyName,
        String before,
        String after
) {
    public static HistoryDiffDto from(HistoryDetail historyDetail) {
        return new HistoryDiffDto(
                historyDetail.getPropertyName(),
                historyDetail.getBeforeValue(),
                historyDetail.getAfterValue()
        );
    }
}
