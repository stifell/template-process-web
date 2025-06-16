package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.processor.WordDOCX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stifell on 02.06.2025
 */
@Service
public class DocumentService {
    @Autowired
    private WordProcessorService wordProcessorService;
    @Autowired
    private FileStorageService fileStorageService;

    public TagMap extractTags(List<FileContentDTO> files) {
        TagMap tagMap = new TagMap();
        files.forEach(file -> {
            try {
                File tempFile = fileStorageService.createTempFile(file.getContent());
                tagMap.putAll(wordProcessorService.writeTagsToSet(new File[]{tempFile}));
            } catch (Exception e) {
                throw new FileProcessingException("Error while processing file: " + file.getFileName(), e);
            }
        });
        return tagMap;
    }

    public List<FileContentDTO> processedDocuments(List<FileContentDTO> files, TagMap tagMap) {
        return files.stream().map(file -> {
            try {
                File inputTempFile = fileStorageService.createTempFile(file.getContent());

                File outputTempFile = File.createTempFile("processed-", ".docx");
                outputTempFile.deleteOnExit();

                WordDOCX.createFile(tagMap, inputTempFile, outputTempFile.getAbsolutePath());

                byte[] processedContent = Files.readAllBytes(outputTempFile.toPath());
                return new FileContentDTO(file.getFileName(), processedContent);
            } catch (IOException e) {
                throw new FileProcessingException("Error while processing file: " + file.getFileName(), e);
            }
        }).collect(Collectors.toList());
    }
}
