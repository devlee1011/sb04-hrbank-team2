package com.codeit.hrbank.change_log.controller;

import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import com.codeit.hrbank.change_log.mapper.ChangeLogMapper;
import com.codeit.hrbank.change_log.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChangeLogController {
    private final ChangeLogService changeLogService;
    private final ChangeLogMapper changeLogMapper;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        ChangeLogDetail changeLogDetail = changeLogService.getChangeLogDetail(id);
        DiffDto response = changeLogMapper.toDto(changeLogDetail);

        return ResponseEntity.ok(response);
    }
}
