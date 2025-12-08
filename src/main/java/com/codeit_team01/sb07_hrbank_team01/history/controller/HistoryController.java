package com.codeit_team01.sb07_hrbank_team01.history.controller;

import com.codeit_team01.sb07_hrbank_team01.common.doc.HistoryControllerDocs;
import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryDiffDto;
import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryType;
import com.codeit_team01.sb07_hrbank_team01.history.repository.HistoryRepository;
import com.codeit_team01.sb07_hrbank_team01.history.service.HistoryService;
import com.codeit_team01.sb07_hrbank_team01.history.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition.*;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class HistoryController implements HistoryControllerDocs {
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    //전체 이력 목록 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<HistoryChangeLogDto>> searchHistories(
            @RequestParam(required = false) String employeeNumber,//
            @RequestParam(required = false) String memo,//
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)Instant atFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)Instant atTo,
            @RequestParam(required = false) HistoryType type,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Integer size
            ){
        HistorySearchCondition condition = HistorySearchCondition.builder()
                .employeeNo(employeeNumber)
                .memo(memo)
                .ipAddress(ipAddress)
                .atFrom(atFrom)
                .atTo(atTo)
                .type(type)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .cursorId(CursorUtils.decodeCursor(cursor))
                .size(size)
                .build();
        PageResponseDto<HistoryChangeLogDto> response = historyService.searchHistories(condition);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/diffs")
    public ResponseEntity<List<HistoryDiffDto>> getHistoryDetails(
            @PathVariable("id") Long historyId
    ){
        List<HistoryDiffDto> diffs = historyService.getHistoryDetail(historyId);
        return ResponseEntity.ok(diffs);
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount(){
        Long count = historyService.getTotalCount();
        return ResponseEntity.ok(count);
    }
}
