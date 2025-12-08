package com.codeit_team01.sb07_hrbank_team01.backup.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseUpdateEntity;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "backups")
public class Backup extends BaseUpdateEntity {
  @Column(name = "worker", nullable = false, length = 50)
  private String worker;

  @Column(name = "start_time")
  private Instant startTime;

  @Column(name = "end_time")
  private Instant endTime;

  @Column(name = "status", length = 30)
  @Enumerated(EnumType.STRING)
  private BackupStatus status;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private MetaFile metaFile;

  public void update(
      String worker,
      BackupStatus status,
      Instant startTime,
      Instant endTime,
      MetaFile metaFile
  ) {
    if (worker != null && !worker.equals(this.worker)) {
      this.status = status;
    }

    if (status != null && status != this.status) {
      this.status = status;
    }
    if (startTime != null && !startTime.equals(this.startTime)) {
      this.startTime = startTime;
    }
    if (endTime != null && !endTime.equals(this.endTime)) {
      this.endTime = endTime;
    }
    if (metaFile != null && !metaFile.equals(this.metaFile)) {
      this.metaFile = metaFile;
    }
  }
}
