package com.codeit_team01.sb07_hrbank_team01.common.doc;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.exception.dto.ErrorResponseDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryDiffDto;
import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정 이력 관리 API")
public interface HistoryControllerDocs {

    @Operation(
            summary = "변경 이력 검색",
            description = "사원 번호, 메모, IP, 기간, 타입 등을 조건으로 변경 이력을 조회합니다.",
            parameters = {
                    @Parameter(name = "employeeNumber", description = "사원 번호"),
                    @Parameter(name = "memo", description = "메모 내용"),
                    @Parameter(name = "ipAddress", description = "IP 주소"),
                    @Parameter(name = "atFrom", description = "검색 시작 시각 (ISO-8601)", schema = @Schema(format = "date-time")),
                    @Parameter(name = "atTo", description = "검색 종료 시각 (ISO-8601)", schema = @Schema(format = "date-time")),
                    @Parameter(name = "type", description = "이력 타입", schema = @Schema(implementation = HistoryType.class)),
                    @Parameter(name = "sortField", description = "정렬 기준", schema = @Schema(implementation = HistorySearchCondition.class)),
                    @Parameter(name = "cursor", description = "커서 (마지막 조회 기준)"),
                    @Parameter(name = "cursor", description = "커서 (마지막 조회 기준)"),
                    @Parameter(name = "size", description = "페이지 크기", schema = @Schema(defaultValue = "10"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    public ResponseEntity<PageResponseDto<HistoryChangeLogDto>> searchHistories(
            @RequestParam(required = false) String employeeNumber,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(required = false) HistoryType type,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Integer size
    );


    @Operation(
            summary = "변경 상세 내역 조회",
            description = "특정 변경 이력 ID의 세부 변경 내용을 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "변경 이력 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoryDiffDto.class)))),
                    @ApiResponse(responseCode = "404", description = "이력을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    public ResponseEntity<List<HistoryDiffDto>> getHistoryDetails(
            @PathVariable("id") Long historyId
    );


    @Operation(
            summary = "전체 변경 이력 수 조회",
            description = "전체 변경 이력의 총 개수를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(type = "integer"))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    public ResponseEntity<Long> getTotalCount();


}
