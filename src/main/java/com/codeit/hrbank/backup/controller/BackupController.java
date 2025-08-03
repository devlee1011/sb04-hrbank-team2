package com.codeit.hrbank.backup.controller;

import com.codeit.hrbank.backup.dto.BackupDto;
import com.codeit.hrbank.backup.entitiy.Backup;
import com.codeit.hrbank.backup.mapper.BackupMapper;
import com.codeit.hrbank.backup.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
