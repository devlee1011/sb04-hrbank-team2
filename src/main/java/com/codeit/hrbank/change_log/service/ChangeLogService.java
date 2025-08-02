package com.codeit.hrbank.change_log.service;

import java.time.Instant;

public interface ChangeLogService {
    Long getCount(Instant fromDate, Instant toDate);
}
