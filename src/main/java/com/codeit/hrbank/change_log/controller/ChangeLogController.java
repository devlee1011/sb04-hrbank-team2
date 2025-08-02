package com.codeit.hrbank.change_log.controller;

import com.codeit.hrbank.change_log.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class ChangeLogController {
    private final ChangeLogService changeLogService;

    @GetMapping("/count")
    public ResponseEntity count(@RequestParam Instant fromDate, @RequestParam Instant toDate) {
        if(fromDate == null) {
            fromDate = Instant.now().minus(7, ChronoUnit.DAYS);
        }
        if(toDate == null) {
            toDate = Instant.now();
        }
        Long count = changeLogService.getCount(fromDate,toDate);
        return ResponseEntity.ok(count);
    }
}
