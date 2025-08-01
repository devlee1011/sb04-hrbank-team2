package com.codeit.hrbank.stored_file.controller;

import com.codeit.hrbank.stored_file.controller.api.StoredFileApi;
import com.codeit.hrbank.stored_file.service.StoredFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class StoredFileController implements StoredFileApi {

  private final StoredFileService storedFileService;

  @Override
  @PostMapping
  public void createStoredFile(@RequestPart(value = "file") MultipartFile file) {
    storedFileService.createStoredFile(file);
  }

  @Override
  @GetMapping("/{id}/download")
  public ResponseEntity downloadSoredFile(@PathVariable("id") Long storedFileId) {
    return storedFileService.downloadSoredFile(storedFileId);
  }
}

