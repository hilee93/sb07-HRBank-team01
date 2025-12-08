package com.codeit_team01.sb07_hrbank_team01.history.service;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistoryEmployeeCopyDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryDiffDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface HistoryService {

    // 직원 생성 이력 등록
    void createHistory(Employee employee, String memo);//, HttpServletRequest request

    // 직원 수정 이력 등록 : 퇴사 포함
    void updateHistory(HistoryEmployeeCopyDto beforeEmployee, Employee afterEmployee, String memo); //, HttpServletRequest request

    // 직원 삭제 이력 등록
    void deleteHistory(Employee employee, String memo); //, HttpServletRequest request

    // 이력 상세 조회
    List<HistoryDiffDto> getHistoryDetail(Long historyId);

    //이력 검색 조회
    PageResponseDto<HistoryChangeLogDto> searchHistories(HistorySearchCondition condition);

    //전체이력갯수
    Long getTotalCount();
}
