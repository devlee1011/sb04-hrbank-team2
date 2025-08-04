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
    int requestedSize = backupGetAllRequest.size() != null ? backupGetAllRequest.size() : 20;

    // hasNext 판단을 위해 1개 더 가져온 경우 잘라줌
    boolean hasNext = backupList.size() > requestedSize;
    if (hasNext) {
      backupList = backupList.subList(0, requestedSize);
    }

    List<BackupDto> content = backupList.stream()
        .map(backupMapper::toDto)
        .toList();

    String nextCursor = null;
    Long nextIdAfter = null;

    if (hasNext) {
      Backup last = backupList.get(backupList.size() - 1);
      nextIdAfter = last.getId();

      String sortField = backupGetAllRequest.sortField() != null
          ? backupGetAllRequest.sortField()
          : "id";

      if ("startedAt".equals(sortField) && last.getStartedAt() != null) {
        nextCursor = last.getStartedAt().toString();
      } else if ("id".equals(sortField)) {
        nextCursor = last.getId().toString();
      }
      // 다른 필드 정렬도 여기서 추가 처리 가능
    }

    return new CursorPageResponseBackupDto(
        content,
        nextCursor,
        nextIdAfter,
        requestedSize,
        (long) content.size(),
        hasNext
    );
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
