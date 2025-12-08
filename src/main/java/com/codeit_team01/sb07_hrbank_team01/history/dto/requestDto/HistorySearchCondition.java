package com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto;

import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class HistorySearchCondition {
    // 검색 조건
    private String employeeNo;
    private String memo;
    private String ipAddress;
    private Instant atFrom;
    private Instant atTo;
    private HistoryType type;

    // 정렬 타입
    private String sortField;
    private String sortDirection;

    //커서ID
    private Long cursorId;
    //페이지 크기
    private Integer size;
    //페이지 크기 반환
    public int getPageSize(){
        return (size != null && size > 0) ? size : 10;
    }

    // sortFiled기본값
    public String getSortField() {
        return (sortField != null) ? sortField : "at";
    }
    public String getSortDirection() {
        return (sortDirection != null) ? sortDirection : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(getSortDirection());
    }
}
