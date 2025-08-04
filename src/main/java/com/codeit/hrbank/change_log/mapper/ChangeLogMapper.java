package com.codeit.hrbank.change_log.mapper;

import com.codeit.hrbank.change_log.dto.ChangeLogDto;
import com.codeit.hrbank.change_log.dto.DiffDto;
import com.codeit.hrbank.change_log.entity.ChangeLog;
import com.codeit.hrbank.change_log.entity.ChangeLogDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {
    @Mapping(target = "at", source = "createdAt")
    ChangeLogDto toDto(ChangeLog changeLog);

    @Mapping(target = "propertyName", source = "fieldName")
    @Mapping(target = "before", source = "oldValue")
    @Mapping(target = "after", source = "newValue")
    DiffDto toDto(ChangeLogDetail changeLogDetail);
}
