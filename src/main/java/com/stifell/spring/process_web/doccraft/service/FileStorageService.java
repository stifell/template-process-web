package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.FileUploadDTO;
import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import com.stifell.spring.process_web.doccraft.exception.InvalidFileTypeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stifell on 02.06.2025
 */
@Service
public class FileStorageService {
    public List<FileUploadDTO> storeFiles(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::validateAndStore)
                .collect(Collectors.toList());
    }

    private FileUploadDTO validateAndStore(MultipartFile file) {
        validateFile(file);
        try {
            return new FileUploadDTO(file.getOriginalFilename(), file.getBytes());
        } catch (Exception e) {
            throw new FileProcessingException("Error reading file " + file.getOriginalFilename(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".docx")) {
            throw new InvalidFileTypeException("File is not docx");
        }
    }

    public File createTempFile(byte[] content) {
        try {
            File tempFile = File.createTempFile("docx-", ".docx");
            Files.write(tempFile.toPath(), content);
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e){
            throw new FileProcessingException("Error creating temp file", e);
        }
    }
}
