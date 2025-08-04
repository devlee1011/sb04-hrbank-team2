package com.codeit.hrbank.stored_file.service;

import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalStoredFileStorage {

  @Value("${hrbank.local.path}")
  private Path localRoot;
  private final String EXTENSION = ".ser";

  @PostConstruct
  void init() {
    try {
      if (Files.notExists(localRoot)) {
        Files.createDirectories(localRoot);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  Path resolve(Long storedFileId) {
    return localRoot.resolve(storedFileId + EXTENSION);
  }

  public Long put(Long storedFileId, byte[] bytes) {
    Path path = resolve(storedFileId);
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return storedFileId;
  }

  public ResponseEntity download(StoredFile storedFile) {
    Resource resource;

    try {
      resource = new UrlResource(resolve(storedFile.getId()).toUri());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + storedFile.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, storedFile.getType())
        .contentLength(storedFile.getSize())
        .body(resource);
  }

  public StoredFile backup(List<Employee> employees, Long backupNumber, StoredFile storedFile) {
    String backup_extension = ".csv";
    Path backupPath = localRoot.resolve(backupNumber + backup_extension);
    try (BufferedWriter writer = Files.newBufferedWriter(backupPath)) {
      writer.write("id,name,email,position,hireDate,status,employeeNumber,departmentId,profileId,createdAt,updatedAt");
      writer.newLine();

      for (Employee employee : employees) {
        StringBuilder backupInfo = new StringBuilder();
        backupInfo.append(employee.getId()).append(",")
            .append(employee.getName()).append(",")
            .append(employee.getEmail()).append(",")
            .append(employee.getPosition()).append(",")
            .append(employee.getHireDate()).append(",")
            .append(employee.getStatus()).append(",")
            .append(employee.getEmployeeNumber()).append(",")
            .append(employee.getDepartment().getId()).append(",")
            .append(employee.getProfile().getId()).append(",")
            .append(employee.getCreatedAt()).append(",")
            .append(employee.getUpdatedAt());

        writer.write(backupInfo.toString());
        writer.newLine();
      }

      // 파일 메타데이터 설정 (파일 다 쓰고 나서)
      storedFile.setFileName(backupNumber + backup_extension);
      storedFile.setType("text/csv");
      storedFile.setSize(Files.size(backupPath));

      throw new IOException();
    } catch (IOException e) {
      String error_extension = ".csv";
      Path errorPath = localRoot.resolve(backupNumber + error_extension);

      try (BufferedWriter writer = Files.newBufferedWriter(errorPath)) {
        StringBuilder errorInfo = new StringBuilder();

        errorInfo.append("Error: ").append(e.getClass().getSimpleName()).append("\n");
        errorInfo.append("Message: ").append(e.getMessage()).append("\n");

        if (e.getStackTrace().length > 0) {
          StackTraceElement top = e.getStackTrace()[0];
          errorInfo.append("Location: ")
              .append(top.getClassName()).append(".")
              .append(top.getMethodName())
              .append(" (").append(top.getFileName())
              .append(":").append(top.getLineNumber()).append(")").append("\n");
        }

        writer.write(errorInfo.toString());

        storedFile.setFileName("ERROR_"+backupNumber + error_extension);
        storedFile.setType("text/plain");
        storedFile.setSize(Files.size(errorPath));

      } catch (IOException ex) {
        throw new RuntimeException("백업 실패 로그 작성중 오류가 발생했습니다.");
      }
    }
    return storedFile;
  }

}