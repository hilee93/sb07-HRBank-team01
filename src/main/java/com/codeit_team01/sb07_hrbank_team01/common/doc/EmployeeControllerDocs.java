package com.codeit_team01.sb07_hrbank_team01.common.doc;

import com.codeit_team01.sb07_hrbank_team01.common.exception.dto.ErrorResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeDistributionResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeePageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeTrendResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.EmployeeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "직원 관리", description = "직원 관련 CRUD 및 조회 API")
public interface EmployeeControllerDocs{
    // ------------------------------
// 직원 등록
// ------------------------------
    @Operation(
            summary = "직원 등록",
            description = "새로운 직원을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "등록 성공",
                            content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )

    EmployeeResponseDto createEmployee(
            EmployeeCreateRequestDto employee,
            MultipartFile profile
    );

    // ------------------------------
// 직원 상세 조회
// ------------------------------
    @Operation(
            summary = "직원 상세 조회",
            description = "ID로 특정 직원을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "직원 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    EmployeeResponseDto getEmployee(Long id);

    // ------------------------------
// 직원 수정
// ------------------------------
    @Operation(
            summary = "직원 수정",
            description = "직원 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공",
                            content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "직원 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeUpdateRequestDto employee,
            MultipartFile profile
    );

    // ------------------------------
// 직원 삭제
// ------------------------------
    @Operation(
            summary = "직원 삭제",
            description = "직원을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공")
            }
    )
    void deleteEmployee(Long id);

    // ------------------------------
// 직원 목록 조회
// ------------------------------
    @Operation(
            summary = "직원 목록 조회",
            description = "필터 조건 및 커서 기반 페이지네이션을 통해 직원 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "nameOrEmail", description = "직원 이름 또는 이메일"),
                    @Parameter(name = "employeeNumber", description = "사원 번호"),
                    @Parameter(name = "departmentName", description = "부서 이름"),
                    @Parameter(name = "position", description = "직함"),
                    @Parameter(name = "hireDateFrom", description = "입사일 시작", schema = @Schema(format = "date")),
                    @Parameter(name = "hireDateTo", description = "입사일 종료", schema = @Schema(format = "date")),
                    @Parameter(name = "status", description = "상태 (ACTIVE, ON_LEAVE, RESIGNED)",
                            schema = @Schema(implementation = EmployeeStatus.class)),
                    @Parameter(name = "idAfter", description = "이전 페이지 마지막 요소 ID"),
                    @Parameter(name = "cursor", description = "커서"),
                    @Parameter(name = "size", description = "페이지 크기", schema = @Schema(defaultValue = "10")),
                    @Parameter(name = "sortField", description = "정렬 필드(name, employeeNumber, hireDate)", schema = @Schema(defaultValue = "name")),
                    @Parameter(name = "sortDirection", description = "정렬 방향(asc, desc)", schema = @Schema(defaultValue = "asc"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = EmployeePageResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    EmployeePageResponseDto getEmployees(
            String nameOrEmail,
            String employeeNumber,
            String departmentName,
            String position,
            LocalDate hireDateFrom,
            LocalDate hireDateTo,
            EmployeeStatus status,
            Long idAfter,
            String cursor,
            int size,
            String sortField,
            String sortDirection
    );

    // ------------------------------
// 직원 증가 추이 통계
// ------------------------------
    @Operation(
            summary = "직원 증감 추이 조회",
            description = "기간별 직원 증감 추이를 조회합니다.",
            parameters = {
                    @Parameter(name = "from", description = "시작일", schema = @Schema(format = "date")),
                    @Parameter(name = "to", description = "종료일", schema = @Schema(format = "date")),
                    @Parameter(name = "unit", description = "단위(day, month, year)", schema = @Schema(defaultValue = "month"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeTrendResponseDto.class))))
            }
    )
    List<EmployeeTrendResponseDto> getEmployeesTrend(
            LocalDate from,
            LocalDate to,
            String unit
    );

    // ------------------------------
// 직원 분포 통계
// ------------------------------
    @Operation(
            summary = "직원 분포 조회",
            description = "부서 또는 상태별 직원 분포를 조회합니다.",
            parameters = {
                    @Parameter(name = "groupBy", description = "그룹 기준(department, status)",
                            schema = @Schema(defaultValue = "department")),
                    @Parameter(name = "status", description = "직원 상태", schema = @Schema(implementation = EmployeeStatus.class))
            }
    )
    List<EmployeeDistributionResponseDto> getEmployeesDistribution(
            String groupBy,
            EmployeeStatus status
    );

    // ------------------------------
// 직원 수 조회
// ------------------------------
    @Operation(
            summary = "직원 수 조회",
            description = "상태 및 기간 조건에 따른 직원 수를 조회합니다.",
            parameters = {
                    @Parameter(name = "status", description = "직원 상태"),
                    @Parameter(name = "fromDate", description = "시작일", schema = @Schema(format = "date")),
                    @Parameter(name = "toDate", description = "종료일", schema = @Schema(format = "date"))
            }
    )
    long getEmployeeCount(
            EmployeeStatus status,
            LocalDate fromDate,
            LocalDate toDate
    );

}