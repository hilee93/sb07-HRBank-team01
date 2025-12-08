package com.codeit_team01.sb07_hrbank_team01.backup.service;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.dto.response.BackupResponseDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import com.codeit_team01.sb07_hrbank_team01.backup.mapper.BackupMapper;
import com.codeit_team01.sb07_hrbank_team01.backup.repository.BackupRepository;
import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.mapper.PageResponseMapper;
import com.codeit_team01.sb07_hrbank_team01.employee.repository.EmployeeRepository;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import com.codeit_team01.sb07_hrbank_team01.file.repository.MetaFileRepository;
import com.codeit_team01.sb07_hrbank_team01.file.storage.FileLocalStorage;
import com.codeit_team01.sb07_hrbank_team01.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackUpServiceImpl implements BackupService {

    private final BackupRepository backupRepository;
    private final HistoryRepository historyRepository;
    private final MetaFileRepository metaFileRepository;
    private final EmployeeRepository employeeRepository;

    private final BackupMapper backupMapper;
    private final PageResponseMapper pageResponseMapper;

    private final FileLocalStorage fileLocalStorage;

    private final CSVCreateService employeeBackupService;


    @Override
    @Transactional
    public BackupResponseDto createBackup(String worker) {
        // 백업 시작 시간
        Instant startTime = Instant.now();

        // 가장 최근 완료된 백업 시간
        // 만약 없으면 Instant MIN으로 백업 시간을 설정
        Optional<Backup> lastBackup = backupRepository.findFirstByOrderByEndTimeDesc();
        Instant lastBackupCreatedAt = lastBackup
                .map(Backup::getEndTime)
                .orElse(Instant.MIN);

        // 가장 최근 수정된 이력시간
        // 만약 없으면 InstantMin으로 이력 시간을 설정
        Instant lastHistoryCreatedAT = historyRepository.findTopByOrderByCreatedAtDesc()
                .map(BaseEntity::getCreatedAt)
                .orElse(Instant.MIN);



        // 백업 진행할 필요가 없음.
        if (lastBackupCreatedAt.isAfter(lastHistoryCreatedAT)) {
            Backup skipeedBackup = new Backup(worker, startTime, Instant.now(), BackupStatus.SKIPPED,
                    null);
            backupRepository.save(skipeedBackup);
            return backupMapper.toDto(skipeedBackup);
        }

        // Backup save, BackupStatus == IN_PROGRESS
        Backup backup = Backup.builder()
                .worker(worker)
                .status(BackupStatus.IN_PROGRESS)
                .build();
        backupRepository.save(backup);


        // 백업 진행
        // 백업 파일 생성
        MetaFile file = null;
        try {
            // 백업 에러 트리거
            boolean trigger = employeeRepository.existsByName("버그 유저");
            if(trigger) throw new RuntimeException("허용되지 않은 버그 이름[버그 유저]가 발견되었습니다");

            file = employeeBackupService.backupEmployeesToCsv(backup.getId());

        } catch (RuntimeException e) {
            MetaFile logFile = generateErrorLogFile(backup.getId(), e);
            backup.update(worker, BackupStatus.FAILED, startTime, Instant.now(), logFile);
            backupRepository.save(backup);

            return backupMapper.toDto(backup);
        }

        backup.update(worker, BackupStatus.COMPLETED, startTime, Instant.now(), file);
        backupRepository.save(backup);

        return backupMapper.toDto(backup);

    }

    @Override
    @Transactional
    public PageResponseDto<BackupResponseDto> getBackupPageList(BackupRequestDto backupRequestDto) {

        // 1. Repository 호출 (Querydsl로 구현된 findBackupPage 실행)
        Page<Backup> backupPage = backupRepository
                .findBackupPage(backupRequestDto);
        // 2. 다음 페이지 커서 정보 추출
        Object nextCursor = null;
        Long nextIdAfter = null;
        if (backupPage.hasNext() && !backupPage.isEmpty()) {

            // 현재 페이지의 마지막 레코드 (커서가 될 레코드)
            List<Backup> content = backupPage.getContent();
            Backup lastBackup = content.get(content.size() - 1);

            String cursor = backupRequestDto.sortField();

            // cursor가 없다면, 이전 정렬필드로부터 다음 커서 가져오도록 보정
            //  Keyset Pagination의 다음 커서 값 설정
            //  기본 정렬 필드: startedAt

            switch (cursor) {
                case "endedAt" -> nextCursor = lastBackup.getEndTime();
                case "status" -> nextCursor = lastBackup.getStatus();
                default -> nextCursor = lastBackup.getStartTime();
            }

            nextIdAfter = lastBackup.getId();
        }

        Page<BackupResponseDto> backupPageDto = backupPage
                .map(backupMapper::toDto);

        long total = backupRepository.countTotalElements(backupRequestDto.worker(), backupRequestDto.status(), backupRequestDto.startedAtTo(), backupRequestDto.startedAtFrom());

        // 3. PageResponseDto로 변환하여 반환
        return pageResponseMapper.toPageResponseDto(backupPageDto, nextCursor, nextIdAfter, total);
    }

    @Override
    public BackupResponseDto getLatestBackup(BackupStatus backupStatus) {
        Backup backup = backupRepository.findFirstByStatusOrderByEndTimeDesc(backupStatus).orElse(null);
        return backupMapper.toDto(backup);
    }

    private MetaFile generateErrorLogFile(Long backupId, Exception e) {
        String fileName = "backup-" + backupId + "-error-" + System.currentTimeMillis() + ".log";
        String content = logToString(e);

        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MetaFile file = MetaFile.builder()
                .name(fileName)
                .type("text/plain")
                .size((long) bytes.length)
                .build();
        metaFileRepository.save(file);
        fileLocalStorage.put(file.getId(), bytes);

        return file;
    }

    private String logToString(Exception e) {
        StringWriter error = new StringWriter();
        e.printStackTrace(new PrintWriter(error));
        return error.toString();
    }

}

