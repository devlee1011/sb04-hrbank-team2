package com.codeit.hrbank.stored_file.service;

import com.codeit.hrbank.exception.BusinessLogicException;
import com.codeit.hrbank.exception.ExceptionCode;
import com.codeit.hrbank.stored_file.entity.StoredFile;
import com.codeit.hrbank.stored_file.repository.StoredFileRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicStoredFileService implements StoredFileService {

  private final StoredFileRepository storedFileRepository;
  private final LocalStoredFileStorage localStoredFileStorage;

  @Override
  public StoredFile createStoredFile(MultipartFile file) {
    StoredFile storedFile = new StoredFile(file);
    StoredFile savedFile = storedFileRepository.save(storedFile);

    try {
      localStoredFileStorage.put(storedFile.getId(), file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return savedFile;
  }

  @Override
  public ResponseEntity downloadSoredFile(Long storedFileId) {
    StoredFile storedFile = storedFileRepository.findById(storedFileId)
        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STOREDFILE_NOT_FOUND));

      return localStoredFileStorage.download(storedFile);
  }
}
