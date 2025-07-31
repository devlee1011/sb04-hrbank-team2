package com.codeit.hrbank.stored_file.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
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
}
