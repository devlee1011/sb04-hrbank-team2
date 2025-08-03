package com.codeit.hrbank.change_log.controller;

import com.codeit.hrbank.base.dto.PageResponse;
import com.codeit.hrbank.change_log.dto.ChangeLogDto;
import com.codeit.hrbank.change_log.dto.request.ChangeLogGetAllRequest;
import com.codeit.hrbank.change_log.dto.response.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.mapper.ChangeLogMapper;
import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import com.codeit.hrbank.change_log.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class ChangeLogController {
    private final ChangeLogService changeLogService;
    private final ChangeLogMapper changeLogMapper;

    @GetMapping
    public ResponseEntity getAll(@ModelAttribute ChangeLogGetAllRequest changeLogGetAllRequest) {
        Page<ChangeLog> changeLogs = changeLogService.getAll(changeLogGetAllRequest);
        if(!changeLogs.hasContent()) {
            return ResponseEntity.ok(PageResponse.from(Page.empty(),null,null));
        }
        Long idAfter = changeLogs.getContent().get(changeLogs.getContent().size() - 1).getId();
        String cursor = null;
        if ("ipAddress".equals(changeLogGetAllRequest.sortField())) {
            cursor = changeLogs.getContent().get(changeLogs.getContent().size() - 1).getIpAddress();
        } else {
            cursor = changeLogs.getContent().get(changeLogs.getContent().size() - 1).getCreatedAt().toString();
        }

        Page<ChangeLogDto> employeeDtos = changeLogs
                .map(changeLogMapper::toDto);
        CursorPageResponseChangeLogDto response = CursorPageResponseChangeLogDto.from(employeeDtos, idAfter, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/diffs")
    public ResponseEntity getDiff(@PathVariable Long id) {
        List<ChangeLogDetail> changeLogDetail = changeLogService.getChangeLogDetail(id);
        List<DiffDto> response = changeLogDetail.stream()
                .map(changeLogMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity count(@RequestParam(required = false) Instant fromDate, @RequestParam(required = false) Instant toDate) {
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
