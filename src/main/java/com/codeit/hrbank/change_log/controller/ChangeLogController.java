package com.codeit.hrbank.change_log.controller;

import com.codeit.hrbank.change_log.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChangeLogController {
    private final ChangeLogService changeLogService;
}
