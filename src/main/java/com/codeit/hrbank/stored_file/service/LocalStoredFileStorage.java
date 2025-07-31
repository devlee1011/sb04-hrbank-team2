package com.codeit.hrbank.stored_file.service;

import com.codeit.hrbank.stored_file.entity.StoredFile;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalStoredFileStorage {
  @Value("${hrbank.local.path}")
  private Path root;

  private final String EXTENSION = ".ser";

  @PostConstruct
  void init() {
    if (Files.notExists(root)) {
        try {
          Files.createDirectories(root);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
  }

  Path resolve(Long storedFileId) {
    return root.resolve(storedFileId + EXTENSION);
  }

  public Long put(Long storedFileId, byte[] bytes) {
    Path path = resolve(storedFileId);
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return storedFileId;
  }

  public ResponseEntity download(StoredFile storedFile){
    Resource resource;

    try {
      resource = new UrlResource(resolve(storedFile.getId()).toUri());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + storedFile.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, storedFile.getType())
        .contentLength(storedFile.getSize())
        .body(resource);
  }
}
