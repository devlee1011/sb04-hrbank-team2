package com.codeit.hrbank.stored_file.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StoredFileApi {
  void createStoredFile(MultipartFile file);
  ResponseEntity downloadSoredFile(Long storedFileId);
}
