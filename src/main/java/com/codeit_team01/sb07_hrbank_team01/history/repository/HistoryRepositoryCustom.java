package com.codeit_team01.sb07_hrbank_team01.history.repository;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;

public interface HistoryRepositoryCustom {
    PageResponseDto<HistoryChangeLogDto> searchHistoriesWithCursor(HistorySearchCondition condition);
}
