package com.codeit.hrbank.backup.mapper;

import com.codeit.hrbank.backup.dto.BackupDto;
import com.codeit.hrbank.backup.entitiy.Backup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper {
  @Mapping(target = "fileId", expression = "java(backup.getStoredFile() == null ? null : backup.getStoredFile().getId())")
  BackupDto toDto(Backup backup);
}
