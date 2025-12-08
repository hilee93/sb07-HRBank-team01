package com.codeit_team01.sb07_hrbank_team01.history.service;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.util.IpUtils;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistoryEmployeeCopyDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto.HistorySearchCondition;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryDiffDto;
import com.codeit_team01.sb07_hrbank_team01.history.entity.History;
import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryType;
import com.codeit_team01.sb07_hrbank_team01.history.repository.HistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    // 직원 생성 이력 등록
    @Transactional
    @Override
    public void createHistory(Employee employee, String memo) {//, HttpServletRequest request
        //IP주소 자동 추출
        String ipAddress = getCurrentRequestIp();

        History history = History.createHistory(HistoryType.CREATED, memo, ipAddress, employee.getEmployeeNo());

        // 전체 필드 추가
        addAllEmployeeDetail(history, null, employee);

        historyRepository.save(history);
    }

    // 직원 수정 이력 등록
    @Transactional
    @Override
    public void updateHistory(HistoryEmployeeCopyDto beforeEmployee, Employee afterEmployee, String memo) { //, HttpServletRequest request
        //IP주소 자동 추출
        String ipAddress = getCurrentRequestIp();

        History history = History.createHistory(HistoryType.UPDATED, memo, ipAddress, afterEmployee.getEmployeeNo());

        // 수정할 필드 추가
        addChangedEmployeeDetails(history, beforeEmployee, afterEmployee);

        historyRepository.save(history);
    }

    // 직원 삭제 이력 등록
    @Transactional
    @Override
    public void deleteHistory(Employee employee, String memo) { //, HttpServletRequest request
        //IP주소 자동 추출
        String ipAddress = getCurrentRequestIp();

        History history = History.createHistory(HistoryType.DELETED, memo, ipAddress, employee.getEmployeeNo());

        // 전체 필드 추가
        addAllEmployeeDetail(history, employee, null);

        historyRepository.save(history);
    }

    // 상세 이력 조회
    @Transactional(readOnly = true)
    @Override
    public List<HistoryDiffDto> getHistoryDetail(Long historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "상세 이력을 찾을 수 없습니다. ID : " + historyId
                ));
        return history.getDetails().stream()
                .map(HistoryDiffDto::from)
                .toList();
    }

    // 전체 조회
    // 조건 조회
    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<HistoryChangeLogDto> searchHistories(HistorySearchCondition condition) {
        return historyRepository.searchHistoriesWithCursor(condition);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getTotalCount() {
        return historyRepository.count();
    }

    // Helper 메서드 : 생성, 삭제 이력
    private void addAllEmployeeDetail(History history, Employee beforeEmployee, Employee afterEmployee) {
        history.addDetail(
                "입사일",
                beforeEmployee != null
                        ? beforeEmployee.getHireDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate().toString()
                        : null,
                afterEmployee != null
                        ? afterEmployee.getHireDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate().toString()
                        : null
        );
        history.addDetail(
                "이름",
                beforeEmployee != null ? beforeEmployee.getName() : null,
                afterEmployee != null ? afterEmployee.getName() : null
        );
        history.addDetail(
                "직함",
                beforeEmployee != null ? beforeEmployee.getJobPosition() : null,
                afterEmployee != null ? afterEmployee.getJobPosition() : null
        );
        history.addDetail(
                "부서명",
                beforeEmployee != null ? beforeEmployee.getDepartment().getName() : null,
                afterEmployee != null ? afterEmployee.getDepartment().getName() : null
        );
        history.addDetail(
                "이메일",
                beforeEmployee != null ? beforeEmployee.getEmail() : null,
                afterEmployee != null ? afterEmployee.getEmail() : null
        );
        history.addDetail(
                "사번",
                beforeEmployee != null ? beforeEmployee.getEmployeeNo() : null,
                afterEmployee != null ? afterEmployee.getEmployeeNo() : null
        );
        history.addDetail(
                "상태",
                beforeEmployee != null ? beforeEmployee.getStatus().toString() : null,
                afterEmployee != null ? afterEmployee.getStatus().toString() : null
        );
    }

    // Helper 메서드 : 수정 이력
    private void addChangedEmployeeDetails(
            History history, HistoryEmployeeCopyDto before, Employee after
    ) {
        addDetailIfChanged(history, "이름", before.name(), after.getName());
        addDetailIfChanged(history, "입사일",
                before.hireDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate(),
                after.getHireDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate());
        addDetailIfChanged(history, "직함", before.jobPosition(), after.getJobPosition());
        addDetailIfChanged(history, "부서명", before.department(), after.getDepartment().getName());
        addDetailIfChanged(history, "이메일", before.email(), after.getEmail());
        addDetailIfChanged(history, "상태", before.status(), after.getStatus());
    }

    private void addDetailIfChanged(
            History history, String propertyName, Object beforeValue, Object afterValue
    ) {
        if (!Objects.equals(beforeValue, afterValue)) {
            history.addDetail(
                    propertyName,
                    beforeValue != null ? beforeValue.toString() : null,
                    afterValue != null ? afterValue.toString() : null
            );
        }
    }

    private String getCurrentRequestIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return IpUtils.getClientIp(request);
        } catch (IllegalStateException e) {
            return "SYSTEM";
        }
    }
}
