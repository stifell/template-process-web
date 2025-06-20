package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import com.stifell.spring.process_web.doccraft.model.Authors;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.processor.BlockProcessor;
import com.stifell.spring.process_web.doccraft.processor.MultiTagProcessor;
import com.stifell.spring.process_web.doccraft.processor.WordDOC;
import com.stifell.spring.process_web.doccraft.processor.WordDOCX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author stifell on 02.06.2025
 */
@Service
public class DocumentService {
    private final WordProcessorService wordProcessorService;
    private final FileStorageService fileStorageService;
    private final MultiTagProcessor multiTagProcessor;

    @Autowired
    public DocumentService(WordProcessorService wordProcessorService, FileStorageService fileStorageService, MultiTagProcessor multiTagProcessor) {
        this.wordProcessorService = wordProcessorService;
        this.fileStorageService = fileStorageService;
        this.multiTagProcessor = multiTagProcessor;
    }

    public TagMap extractTags(List<FileContentDTO> files, int authorCount) {
        TagMap tagMap = new TagMap();
        files.forEach(file -> {
            File tempFile = null;
            File processedTempFile = null;
            try {
                tempFile = fileStorageService.createTempFile(file.getContent());
                File fileToProcess = tempFile;

                if (file.getFileName().toLowerCase().contains("block_")) {
                    processedTempFile = File.createTempFile("block-processed-", ".docx");
                    BlockProcessor processor = new BlockProcessor(tempFile, authorCount);
                    processor.processBlockFile(processedTempFile.getAbsolutePath());

                    fileToProcess = processedTempFile;
                    file.setContent(Files.readAllBytes(processedTempFile.toPath()));
                }
                tagMap.putAll(wordProcessorService.writeTagsToSet(new File[]{fileToProcess}, authorCount));
            } catch (Exception e) {
                throw new FileProcessingException("Error while processing file: " + file.getFileName(), e);
            } finally {
                if (tempFile != null) tempFile.delete();
                if (processedTempFile != null) processedTempFile.delete();
            }
        });
        return tagMap;
    }

    public List<FileContentDTO> processedDocuments(List<FileContentDTO> files, TagMap tagMap, int authorCount) {
        List<FileContentDTO> result = new ArrayList<>();
        Authors authors = new Authors(authorCount);
        TagMap specialTagMap = tagMap.copyTagMap();

        for (FileContentDTO file : files) {
            String fileName = file.getFileName().toLowerCase();

            if (fileName.contains("main_")) {
                result.add(processMainFile(file, tagMap));
            } else if (fileName.contains("multi_")) {
                result.addAll(processMultiFile(file, specialTagMap, authors));
            } else {
                FileContentDTO processedFile = processWithWordProcessor(file, tagMap);
                processedFile.setFileName(cleanFileName(file.getFileName()));
                result.add(processedFile);
            }
        }
        return result;
    }

    private List<FileContentDTO> processMultiFile(FileContentDTO file, TagMap specialTagMap, Authors authors) {
        List<FileContentDTO> result = new ArrayList<>();
        multiTagProcessor.fillAuthorsTags(specialTagMap, authors);

        for (int i = 0; i < authors.getAuthorCount(); i++) {
            TagMap authorTagMap = new TagMap();
            authorTagMap.putAll(specialTagMap);
            authorTagMap.putAll(authors.getTagMapByIndex(i));

            FileContentDTO processedFile = processWithWordProcessor(file, authorTagMap);

            String cleanedName = cleanFileName(processedFile.getFileName()).replace(".docx", "");
            int authorNumber = i + 1;
            String lastName = authors.getTagMapByIndex(i).entrySet().stream()
                    .filter(e -> e.getKey().matches("\\$\\{key_ria_author[1-9]_lastname\\}"))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse("");
            String newName = String.format("%d. %s_%s.docx",
                    authorNumber,
                    cleanedName,
                    lastName);

            processedFile.setFileName(newName);
            result.add(processedFile);
        }

        return result;
    }

    private FileContentDTO processMainFile(FileContentDTO file, TagMap tagMap) {
        FileContentDTO result = processWithWordProcessor(file, tagMap);
        result.setFileName(cleanFileName(file.getFileName()));
        return result;
    }

    private FileContentDTO processWithWordProcessor(FileContentDTO file, TagMap tagMap) {
        try {
            File inputTempFile = fileStorageService.createTempFile(file.getContent());

            File outputTempFile = File.createTempFile("processed-", ".docx");

            if (inputTempFile.getName().toLowerCase().endsWith(".doc")) {
                WordDOC.createFile(tagMap, inputTempFile, outputTempFile.getAbsolutePath());
            } else {
                WordDOCX.createFile(tagMap, inputTempFile, outputTempFile.getAbsolutePath());
            }

            byte[] processedContent = Files.readAllBytes(outputTempFile.toPath());
            return new FileContentDTO(file.getFileName(), processedContent);
        } catch (IOException e) {
            throw new FileProcessingException("Error while processing file: " + file.getFileName(), e);
        }
    }

    private String cleanFileName(String fileName) {
        return fileName.replaceFirst("^(main_|additional_|multi_|block_)", "");
    }
}