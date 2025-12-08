package com.codeit_team01.sb07_hrbank_team01.backup.Scheduler;

import com.codeit_team01.sb07_hrbank_team01.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackupScheduler {
  private final BackupService backupService;
  /**
   * 1시간마다 주기적으로 실행되는 스케줄링 메서드입니다.
   * Cron 표현식: 0분 0초 (매시 정각) / 매 시간 / 매일 / 매월 / 매년
   */
  @Scheduled(cron = "0 0 * * * *") // 매 시간 정각에 실행
  public void runHourlySystemBackup() {
    log.info("[Scheduler] 시스템 백업 생성 작업 시작");

    try {
      // worker 값에 'system'을 주입하여 createBackup 메서드 호출
      backupService.createBackup("system");

      log.info("[Scheduler] 시스템 백업 생성 작업 시작");

    } catch (Exception e) {
      throw new RuntimeException("스케줄 에러 발생");
    }
  }
}
