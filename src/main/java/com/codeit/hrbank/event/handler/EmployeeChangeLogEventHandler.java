package com.codeit.hrbank.event.handler;

import com.codeit.hrbank.change_log.service.ChangeLogService;
import com.codeit.hrbank.event.EmployeeLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmployeeChangeLogEventHandler {
    private final ChangeLogService changeLogService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmployeeLogEvent event){
        // changeLogService.create(event);
    }
}
