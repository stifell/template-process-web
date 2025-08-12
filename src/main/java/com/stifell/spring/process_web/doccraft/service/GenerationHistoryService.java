package com.stifell.spring.process_web.doccraft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.entity.GenerationHistory;
import com.stifell.spring.process_web.doccraft.entity.HistoryFile;
import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.repository.GenerationHistoryRepository;
import com.stifell.spring.process_web.doccraft.repository.HistoryFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author stifell on 13.08.2025
 */
@Service
public class GenerationHistoryService {
    private final GenerationHistoryRepository historyRepo;
    private final HistoryFileRepository fileRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public GenerationHistoryService(GenerationHistoryRepository historyRepo, HistoryFileRepository fileRepo, ObjectMapper objectMapper) {
        this.historyRepo = historyRepo;
        this.fileRepo = fileRepo;
        this.objectMapper = objectMapper;
    }


    public void saveHistory(User user, int authorCount, List<String> fileNames,
                            TagMap tagMap, List<FileContentDTO> files, Long packageId) {
        try {
            GenerationHistory history = new GenerationHistory();
            history.setUser(user);
            history.setAuthorCount(authorCount);
            history.setPackageId(packageId);
            history.setFileNames(objectMapper.writeValueAsString(fileNames));
            history.setTagMapJson(objectMapper.writeValueAsString(tagMap));
            //TODO: подтягивать файлы из пакетов (при выборе), а не сохранять их в БД

            GenerationHistory savedHistory = historyRepo.save(history);

            for (FileContentDTO file : files) {
                HistoryFile historyFile = new HistoryFile();
                historyFile.setHistory(savedHistory);
                historyFile.setFileName(file.getFileName());
                historyFile.setContent(file.getContent());
                fileRepo.save(historyFile);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации тегов", e);
        }
    }

    public TagMap deserializeTagMap(String json) {
        try {
            return objectMapper.readValue(json, TagMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации тегов", e);
        }
    }

    public List<String> deserializeFileNames(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации списка файлов", e);
        }
    }

    public List<GenerationHistory> getHistory(User user) {
        return historyRepo.findByUserOrderByGenerationDateDesc(user);
    }

    public GenerationHistory getHistoryById(long id) {
        return historyRepo.findById(id).orElseThrow(() -> new RuntimeException("История не найдена"));
    }

    public List<HistoryFile> getHistoryFiles(long historyId) {
        return fileRepo.findByHistoryId(historyId);
    }
}
