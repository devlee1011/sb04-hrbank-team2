package com.codeit.hrbank.change_log.controller;

import com.codeit.hrbank.change_log.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChangeLogController {
    private final ChangeLogService changeLogService;

    @GetMapping("/{id}")
    public DiffDto get(@PathVariable("id") Long changeLogId){

    }
}
