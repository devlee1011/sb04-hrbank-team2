package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.dto.request.ChangeLogGetAllRequest;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import org.springframework.data.domain.Page;

public interface ChangeLogService {
    Page<ChangeLog> getAll(ChangeLogGetAllRequest changeLogGetAllRequest);
}
