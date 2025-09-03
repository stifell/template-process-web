package com.stifell.spring.process_web.doccraft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.dto.GenerationRequestDTO;
import com.stifell.spring.process_web.doccraft.dto.TagFieldDTO;
import com.stifell.spring.process_web.doccraft.dto.UploadState;
import com.stifell.spring.process_web.doccraft.entity.GenerationHistory;
import com.stifell.spring.process_web.doccraft.entity.HistoryFile;
import com.stifell.spring.process_web.doccraft.entity.TagMetadata;
import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.service.GenerationHistoryService;
import com.stifell.spring.process_web.doccraft.service.TagMetadataService;
import com.stifell.spring.process_web.doccraft.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stifell on 13.08.2025
 */
@Controller
@RequestMapping("/history")
public class HistoryController {
    private final GenerationHistoryService historyService;
    private final UserService userService;
    private final TagMetadataService tagMetadataService;

    @Autowired
    public HistoryController(GenerationHistoryService historyService, UserService userService, TagMetadataService tagMetadataService) {
        this.historyService = historyService;
        this.userService = userService;
        this.tagMetadataService = tagMetadataService;
    }

    @GetMapping
    public String historyPage(Model model, HttpServletRequest request, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<GenerationHistory> historyList = historyService.getHistory(user);

        model.addAttribute("historyList", historyList);
        model.addAttribute("historyFileNames", historyList.stream()
                .collect(Collectors.toMap(
                        GenerationHistory::getId,
                        h -> historyService.deserializeFileNames(h.getFileNames())
                )));
        model.addAttribute("currentURI", request.getRequestURI());
        return "history-page";
    }

    @GetMapping("/{id}")
    public String historyDetail(@PathVariable Long id, Model model, HttpSession session, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        GenerationHistory history = historyService.getHistoryByIdForUser(id, currentUser);
        List<FileContentDTO> files = historyService.getHistoryFiles(history);

        UploadState uploadState = new UploadState();
        uploadState.setAuthorCount(history.getAuthorCount());
        uploadState.setPackageId(history.getPackageId());
        uploadState.setFileNames(historyService.deserializeFileNames(history.getFileNames()));
        uploadState.setFileUploaded(true);

        TagMap tagMap = historyService.deserializeTagMap(history.getTagMapJson());
        List<TagFieldDTO> fields = tagMap.entrySet().stream()
                .map(entry -> {
                    String tag = entry.getKey();
                    String value = entry.getValue();
                    TagMetadata metadata = tagMetadataService.find(tag); // Получаем метаданные
                    return new TagFieldDTO(tag, value, metadata.getHint(), metadata.getExample());
                })
                .collect(Collectors.toList());
        uploadState.setFields(fields);

        session.setAttribute("uploadState", uploadState);
        session.setAttribute("authorCount", history.getAuthorCount());
        session.setAttribute("generationData", new GenerationRequestDTO(tagMap, files));

        return "redirect:/upload";
    }
}
