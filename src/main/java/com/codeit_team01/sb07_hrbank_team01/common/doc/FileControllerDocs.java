package com.codeit_team01.sb07_hrbank_team01.common.doc;

import com.codeit_team01.sb07_hrbank_team01.common.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "파일 관리", description = "파일 관리 API")
public interface FileControllerDocs {


    @Operation(
            summary = "파일 다운로드",
            description = "파일 ID로 파일을 다운로드합니다.",
            parameters = {
                    @Parameter(name = "id", description = "다운로드할 파일의 ID")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "다운로드 성공",
                            content = @Content(
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "파일을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                    )
            }
    )
    public ResponseEntity<Resource> download(Long id);


}
