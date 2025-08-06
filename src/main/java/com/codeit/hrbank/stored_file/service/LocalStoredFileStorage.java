package com.codeit.hrbank.stored_file.service;

import com.codeit.hrbank.employee.entity.Employee;
import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
      throw new BusinessLogicException(ExceptionCode.FILE_IO_INTERRUPTED);
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
      throw new BusinessLogicException(ExceptionCode.FILE_IO_INTERRUPTED);
    }
    return storedFileId;
  }

  public ResponseEntity download(StoredFile storedFile) {
    StringBuilder fileNameBuilder = new StringBuilder().append(storedFile.getId());

    if (storedFile.getFileName().endsWith(".csv")) {
      fileNameBuilder.append(".csv");
    } else if (storedFile.getFileName().endsWith(".log")) {
      fileNameBuilder.append(".log");
    } else {
      fileNameBuilder.append(".ser");
    }

    Path filePath = localRoot.resolve(fileNameBuilder.toString());

    Resource resource;
    try {
      resource = new UrlResource(filePath.toUri());
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + storedFile.getFileName() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, storedFile.getType())
          .contentLength(resource.contentLength())
          .body(resource);
    } catch (IOException e) {
      throw new BusinessLogicException(ExceptionCode.FILE_IO_INTERRUPTED);
    }
  }

  public StoredFile backup(List<Employee> employees, Long storedFileNumber, StoredFile storedFile) {
    String extension = ".csv";
    Path path = localRoot.resolve(storedFileNumber + extension);

    try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write("\uFEFF");
      writer.write("id,name,email,position,hireDate,status,employeeNumber,departmentId,profileId,createdAt,updatedAt");
      writer.newLine();

      StringBuilder backupInfo = new StringBuilder();
      for (Employee employee : employees) {
        backupInfo.append(employee.getId()).append(",")
            .append(employee.getName()).append(",")
            .append(employee.getEmail()).append(",")
            .append(employee.getPosition()).append(",")
            .append(employee.getHireDate()).append(",")
            .append(employee.getStatus()).append(",")
            .append(employee.getEmployeeNumber()).append(",")
            .append(employee.getDepartment().getId()).append(",")
            .append(employee.getProfile() == null ? null : employee.getProfile().getId()).append(",")
            .append(employee.getCreatedAt()).append(",")
            .append(employee.getUpdatedAt())
            .append("\n");
      }
      writer.write(backupInfo.toString());
      writer.newLine();

      // 파일 메타데이터 설정 (파일 다 쓰고 나서)
      storedFile.setType("text/csv");
    } catch (IOException e) {
      extension = ".log";
      path = localRoot.resolve(storedFileNumber + extension);

      try (BufferedWriter writer = Files.newBufferedWriter(path,StandardCharsets.UTF_8)) {
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

        storedFile.setType("text/plain");
      } catch (IOException ex) {
        throw new BusinessLogicException(ExceptionCode.FILE_IO_INTERRUPTED);
      }
    }

    try {
      storedFile.setFileName(storedFileNumber + extension);
      storedFile.setSize(Files.size(path));
    } catch (IOException e) {
      throw new BusinessLogicException(ExceptionCode.FILE_IO_INTERRUPTED);
    }

    return storedFile;
  }

}