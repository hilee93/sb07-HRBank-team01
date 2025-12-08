package com.codeit_team01.sb07_hrbank_team01.backup.service;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.employee.repository.EmployeeRepository;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import com.codeit_team01.sb07_hrbank_team01.file.repository.MetaFileRepository;
import com.codeit_team01.sb07_hrbank_team01.file.storage.FileLocalStorage;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CSVCreateService {

  private final EmployeeRepository employeeRepository;
  private final FileLocalStorage fileLocalStorage;
  private final MetaFileRepository metaFileRepository;

  // application.yml 에서 경로를 주입
  @Value("${HRBank.storage.local.backup-path}")
  private String backupPath;

  @Transactional
  public MetaFile backupEmployeesToCsv(Long id) {
    // 1. 실제 저장 경로를 Path API로 안전하게 조립
    String fileName = "employee-backup-" + id + ".csv";
    Path fullPath = Paths.get(backupPath, fileName);
    long fileSize = 0;

    MetaFile metaFile = MetaFile.builder()
        .name(fullPath.getFileName().toString())
        .type("text/csv")
        .size(fileSize)
        .build();
    metaFileRepository.save(metaFile);

    // 2. fileLocalStorage에서 Writer를 받아와서 CSV를 기록
    try (
        Writer writer = fileLocalStorage.getWriter(metaFile.getId()); // 별도의 FileWriter 직접 사용 안함
        CSVWriter csvWriter = new CSVWriter(writer);
        Stream<Employee> employeeStream = employeeRepository.streamAll()
    ) {
      // 3. 헤더
      csvWriter.writeNext(new String[]{
          "ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태"
      });

      // 4. 데이터를 한 줄 씩 기록 (OOM 방지)
      employeeStream.forEach(employee -> {
        csvWriter.writeNext(new String[]{
            employee.getId().toString(),
            employee.getEmployeeNo(),
            employee.getName(),
            employee.getEmail(),
            (employee.getDepartment() != null ? employee.getDepartment().getName() : ""),
            employee.getJobPosition(),
            (employee.getHireDate() != null
                ? employee.getHireDate().atZone(ZoneId.systemDefault()).toLocalDate().toString()
                : ""),
            (employee.getStatus() != null ? employee.getStatus().name() : "")
        });
      });

      writer.flush();
      // 5. 파일 크기 측정은 fileLocalStorage에 따라 다름 → Storage에서 별도 제공 시 보완

      fileSize = fileLocalStorage.size(metaFile.getId());
    } catch (IOException e) {
        throw new RuntimeException("CSV 백업 실패: " + e.getMessage(), e);
    }
    // 6. DB에 파일 정보 저장 (파일명은 Path에서 추출)
    metaFile.updateSize(fileSize);

    metaFileRepository.save(metaFile);
    return metaFile;
  }
}



