package com.codeit.hrbank.stored_file.service;

import com.codeit.hrbank.stored_file.entity.StoredFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StoredFileService {
  StoredFile createStoredFile(MultipartFile file);

  ResponseEntity downloadSoredFile(Long storedFileId);
}
