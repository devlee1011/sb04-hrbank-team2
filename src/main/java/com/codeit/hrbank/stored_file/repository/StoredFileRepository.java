package com.codeit.hrbank.stored_file.repository;

import com.codeit.hrbank.stored_file.entity.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
}
