package com.codeit.hrbank.change_log.service;

import com.codeit.hrbank.change_log.entity.ChangeLogDetail;

public interface ChangeLogService {
    ChangeLogDetail getChangeLogDetail(Long id);
}
