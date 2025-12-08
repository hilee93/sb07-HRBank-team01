package com.codeit_team01.sb07_hrbank_team01.common.doc;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.response.BackupResponseDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Tag(name = "백업 관리", description = "백업 생성 및 조회")
public interface BackupControllerDocs {

    // ------------------------------
// 최신 백업 조회
// ------------------------------
    @Operation(
            summary = "최신 백업 조회",
            description = "상태 조건(선택)에 맞는 가장 최근 백업 1건을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BackupResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<BackupResponseDto> getLatestBackup(
            @Parameter(description = "백업 상태 필터(옵션)") BackupStatus status
    );

    // ------------------------------
// 데이터 백업 목록 조회(커서 기반)
// ------------------------------
    @Operation(
            summary = "데이터 백업 목록 조회",
            description = "커서 기반으로 백업 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<PageResponseDto<BackupResponseDto>> getBackupsByCursor(
            @Parameter(description = "작업자 호스트/IP (like 검색)") String worker,
            @Parameter(description = "백업 상태") BackupStatus status,
            @Parameter(description = "시작일시 From (ISO-8601)") Instant startedAtFrom,
            @Parameter(description = "시작일시 To (ISO-8601)") Instant startedAtTo,
            @Parameter(description = "이전 페이지 마지막 ID") Long idAfter,
            @Parameter(description = "커서(정렬 키 기준)") String cursor,
            @Parameter(description = "페이지 크기", schema = @Schema(defaultValue = "10")) Integer size,
            @Parameter(description = "정렬 필드(startedAt|id)", schema = @Schema(defaultValue = "startedAt")) String sortFiled,
            @Parameter(description = "정렬 방향(asc|desc)", schema = @Schema(defaultValue = "desc")) String sortDirection
    );

    // ------------------------------
// 데이터 백업 생성
// ------------------------------
    @Operation(
            summary = "데이터 백업 생성",
            description = "새로운 백업 작업을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "백업 생성 성공",
                    content = @Content(schema = @Schema(implementation = BackupResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 진행중인 백업이 있음",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<BackupResponseDto> createBackup(HttpServletRequest request);

}
