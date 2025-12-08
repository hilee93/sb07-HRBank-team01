package com.codeit_team01.sb07_hrbank_team01.common.doc;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentSearchRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.response.DepartmentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

@Tag(name = "부서 관리", description = "직원 관련 CRUD 및 조회 API")
public interface DepartmentControllerDocs {



    @Operation(
            summary = "부서 등록",
            description = """
                    부서를 새로 생성합니다.
                    
                    **요청 데이터**
                    - name (String): 부서 이름
                    - description (String): 부서 설명
                    - establishedDate (LocalDate, yyyy-MM-dd)
                    
                    **응답**
                    - 생성된 Department 정보가 반환됩니다.
                    """
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "부서 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Department.class),
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "id": 1,
                                                  "name": "개발팀",
                                                  "description": "소프트웨어 개발을 담당하는 부서입니다.",
                                                  "establishedDate": "2023-01-01",
                                                  "employeeCount": 10
                                                }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 중복된 이름",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Department.class),
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "timestamp": "2025-03-06T05:39:06.152068Z",
                                                  "status": 400,
                                                  "message": "잘못된 요청입니다.",
                                                  "details": "부서 코드는 필수입니다."
                                                }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Department.class),
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "timestamp": "2025-03-06T05:39:06.152068Z",
                                                  "status": 500,
                                                  "message": "잘못된 요청입니다.",
                                                  "details": "부서 코드는 필수입니다."
                                                }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<DepartmentResponseDto> createDepartment(DepartmentCreateRequestDto request);


    @Operation(
            summary = "부서 단건 조회",
            description = """
            부서 ID로 부서 정보를 조회합니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentResponseDto.class),
                            examples = @ExampleObject(value = """
                {
                  "id": 1,
                  "name": "인사팀",
                  "description": "채용 및 복지 담당 부서",
                  "establishedDate": "2020-05-10",
                  "employeeCount": 12,
                  "createdAt": "2025-12-05T09:30:00Z",
                  "updatedAt": "2025-12-05T10:00:00Z"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "부서를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T09:30:00Z",
                  "status": 400,
                  "message": "잘못된 요청입니다.",
                  "details": "부서 코드는 필수입니다."
                }
            """)
                    )
            )
    })
    public ResponseEntity<DepartmentResponseDto> getDepartment(Long departmentId);


    @Operation(
            summary = "부서 수정",
            description = """
            부서 정보를 수정합니다.
            
            **요청 데이터**
            - name (String): 부서 이름
            - description (String): 부서 설명
            - establishedDate (LocalDate, yyyy-MM-dd)
            """
    )
    @RequestBody(
            required = true,
            description = "부서 수정 요청",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DepartmentUpdateRequestDto.class),
                    examples = @ExampleObject(value = """
            {
              "name": "인사혁신팀",
              "description": "채용/평가/복지 통합 관리",
              "establishedDate": "2021-01-01"
            }
        """)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentResponseDto.class),
                            examples = @ExampleObject(value = """
                            {
                              "id": 1,
                              "name": "인사혁신팀",
                              "description": "채용/평가/복지 통합 관리",
                              "establishedDate": "2021-01-01",
                              "employeeCount": 13
                            }
                            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청(형식/필수값 누락)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T11:00:00Z",
                  "status": 400,
                  "message": "잘못된 요청입니다.",
                  "details": "부서 코드는 필수입니다."
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "부서를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T11:00:00Z",
                  "status": 400,
                  "message": "잘못된 요청입니다.",
                  "details": "부서 코드는 필수입니다."
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "중복 이름 등 무결성 충돌",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T11:00:00Z",
                  "status": 500,
                  "message": "잘못된 요청입니다.",
                  "details": "부서 코드는 필수입니다."
                }
            """)
                    )
            )
    })
    public ResponseEntity<DepartmentResponseDto> updateDepartment(Long departmentId, DepartmentUpdateRequestDto request);

    @Operation(
            summary = "부서 삭제",
            description = """
            부서를 삭제합니다.
            직원이 소속된 부서는 삭제할 수 없습니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공 (본문 없음)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "부서를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T12:00:00Z",
                  "status": 404,
                  "message": "Not Found",
                  "details": ["부서를 찾을 수 없습니다: 123"]
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "직원 소속으로 인한 삭제 불가",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T12:00:00Z",
                  "status": 409,
                  "message": "데이터 무결성 위반",
                  "details": ["소속 직원이 존재하여 삭제할 수 없습니다."]
                }
            """)
                    )
            )
    })
    public ResponseEntity<Void> deleteDepartment(Long departmentId);


    @Operation(
            summary = "부서 목록 조회",
            description = """
            커서 기반으로 부서 목록을 조회합니다.
            
            **요청 데이터**
            - nameOrDescription (String, optional)
            - size (Integer, default 10)
            - sortField (name|establishedDate, default name)
            - sortDirection (asc|desc, default asc)
            - cursor (String, 정렬필드 기준 커서)
            - idAfter (Long, 동일 정렬 키일 때 tie-breaker)
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponseDto.class),
                            examples = @ExampleObject(value = """
                {
                  "content": [
                    {
                      "id": 11,
                      "name": "인사팀",
                      "description": "채용 및 복지 담당 부서",
                      "establishedDate": "2020-05-11",
                      "employeeCount": 9,
                      "createdAt": "2025-12-01T00:00:00Z",
                      "updatedAt": "2025-12-05T10:00:00Z"
                    },
                    {
                      "id": 12,
                      "name": "재무팀",
                      "description": "회계/정산",
                      "establishedDate": "2020-05-12",
                      "employeeCount": 7,
                      "createdAt": "2025-12-01T00:00:00Z",
                      "updatedAt": "2025-12-05T10:00:00Z"
                    }
                  ],
                  "nextCursor": "2020-05-12",
                  "nextIdAfter": 12,
                  "size": 5,
                  "totalElements": 37,
                  "hasNext": true
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청(형식/범위 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-12-05T12:30:00Z",
                  "status": 400,
                  "message": "잘못된 요청입니다.",
                  "details": [
                    "size: 1 이상이어야 합니다.",
                    "sortField: name | establishedDate 만 가능합니다."
                  ]
                }
            """)
                    )
            )
    })
    public ResponseEntity<PageResponseDto<DepartmentResponseDto>> getDepartmentsByCursor(DepartmentSearchRequestDto request);

}
