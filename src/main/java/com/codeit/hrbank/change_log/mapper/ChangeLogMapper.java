package com.codeit.hrbank.change_log.mapper;

import com.codeit.hrbank.change_log.dto.ChangeLogDto;
import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import org.mapstruct.Mapper;

@Mapper
public interface ChangeLogMapper {
    ChangeLogDto toDto(ChangeLog changeLog);
    DiffDto toDto(ChangeLogDetail changeLogDetail);
}
