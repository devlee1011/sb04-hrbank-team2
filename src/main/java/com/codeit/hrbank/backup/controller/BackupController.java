package com.codeit.hrbank.backup.controller;

import com.codeit.hrbank.backup.dto.BackupDto;
import com.codeit.hrbank.backup.dto.request.BackupGetAllRequest;
import com.codeit.hrbank.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.mapper.BackupMapper;
import com.codeit.hrbank.backup.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupController {

  private final BackupService backupService;
  private final BackupMapper backupMapper;

  @PostMapping
  public BackupDto createBackup(HttpServletRequest request) {
    String requestIp = getClientIp(request);
    Backup backup = backupService.createBackup(requestIp);

    return backupMapper.toDto(backup);
  }

  @GetMapping()
  public CursorPageResponseBackupDto getAllBackups(
      @ModelAttribute("backupGetAllRequest") BackupGetAllRequest backupGetAllRequest) {

    Page<Backup> backups = backupService.getAllBackups(backupGetAllRequest);

    List<Backup> backupList = backups.getContent();
    int requestedSize = backupGetAllRequest.size() != null ? backupGetAllRequest.size() : 10;

    boolean hasNext = backupList.size() > requestedSize;
    String nextCursor = null;
    Long nextIdAfter = null;

    if (hasNext) {
      Backup last = backupList.get(backupList.size() - 1);

      String sortField = backupGetAllRequest.sortField() != null ? backupGetAllRequest.sortField() : "startedAt";
      switch (sortField) {
        case "endedAt"   -> nextCursor = String.valueOf(last.getEndedAt());
        case "status"    -> nextCursor = last.getStatus().name();
        default -> nextCursor = String.valueOf(last.getStartedAt());
      }

      backupList = backupList.subList(0, requestedSize);
      nextIdAfter = backupList.get(backupList.size() - 1).getId();
    }

    List<BackupDto> content = backupList.stream()
        .map(backupMapper::toDto)
        .toList();

    return CursorPageResponseBackupDto.from(content,nextCursor,nextIdAfter,backups.getTotalElements(),hasNext);
  }

  @GetMapping("/latest")
  public BackupDto getLatestBackup(@RequestParam(required = false, defaultValue = "COMPLETED") String status) {
    Backup backup = backupService.getLatestBackup(status);
    return backupMapper.toDto(backup);
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    // IPv6 로컬 loopback → IPv4로 변환
    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
      ip = "127.0.0.1";
    }

    return ip;
  }
}
