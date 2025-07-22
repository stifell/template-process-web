package com.stifell.spring.process_web.doccraft.controller;

import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.dto.GenerationRequestDTO;
import com.stifell.spring.process_web.doccraft.dto.TagFieldDTO;
import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import com.stifell.spring.process_web.doccraft.exception.InvalidFileTypeException;
import com.stifell.spring.process_web.doccraft.exception.ResourceNotFoundException;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author stifell on 28.05.2025
 */
@Controller
public class DocumentController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ZipService zipService;
    @Autowired
    private TagMetadataService tagMetadataService;
    @Autowired
    private CsvExportService csvExportService;
    @Autowired
    private CsvImportService csvImportService;

    @GetMapping("/upload")
    public String getUploadPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "upload-page";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile[] files,
                                   @RequestParam("authorCount") int authorCount,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        try {
            session.setAttribute("authorCount", authorCount);
            List<FileContentDTO> uploadedFiles = fileStorageService.storeFiles(files);
            TagMap tagMap = documentService.extractTags(uploadedFiles, authorCount);

            session.setAttribute("generationData", new GenerationRequestDTO(tagMap, uploadedFiles));

            redirectAttributes.addFlashAttribute("fileNames",
                    uploadedFiles.stream()
                            .map(FileContentDTO::getFileName)
                            .collect(Collectors.toList()));

            redirectAttributes.addFlashAttribute("tags", tagMap.keySet());
            redirectAttributes.addFlashAttribute("tagMap", tagMap);
            redirectAttributes.addFlashAttribute("fileUploaded", true);
            List<TagFieldDTO> fields = tagMap.keySet().stream().map(tag ->
            {
                String value = tagMap.get(tag);
                var md = tagMetadataService.find(tag);
                return new TagFieldDTO(tag, value, md.getHint(), md.getExample());
            }).collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("fields", fields);

        } catch (InvalidFileTypeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (FileProcessingException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка обработки файлов: " + e.getMessage());
        }

        return "redirect:/upload";
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateDocument(@RequestParam Map<String, String> formParams,
                                                     HttpSession session) {

        int authorCount = (int) session.getAttribute("authorCount");
        GenerationRequestDTO generationData =
                (GenerationRequestDTO) session.getAttribute("generationData");

        if (generationData == null || generationData.getFiles() == null) {
            throw new ResourceNotFoundException("Данные для генерации не найдены");
        }

        TagMap tagMap = generationData.getTagMap();
        tagMap.replaceAll(formParams::getOrDefault);

        List<FileContentDTO> processedDocs = documentService.processedDocuments(
                generationData.getFiles(),
                tagMap,
                authorCount
        );

        Resource zipResource = zipService.createZipArchive(processedDocs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documents.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipResource);
    }

    @GetMapping("/tags/export")
    public ResponseEntity<Resource> exportTags(HttpSession session) {
        @SuppressWarnings("unchecked")
        TagMap tagMap = ((GenerationRequestDTO) session.getAttribute("generationData")).getTagMap();
        if (tagMap == null) {
            throw new ResourceNotFoundException("Нет тегов для экспорта в csv");
        }
        ByteArrayResource csv = csvExportService.exportyTagsAsCsv(tagMap);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tags.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv);
    }

    @PostMapping("/tags/import")
    public String importTags(@RequestParam("csvFile") MultipartFile csvFile,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            List<TagFieldDTO> imported = csvImportService.importFromCsv(csvFile);

            GenerationRequestDTO generationData = (GenerationRequestDTO) session.getAttribute("generationData");
            TagMap tagMap = generationData.getTagMap();

            imported.forEach(f -> tagMap.put(f.getTag(), f.getValue()));
            session.setAttribute("generationData", generationData);

            List<TagFieldDTO> fields = imported.stream()
                            .map(f -> {
                                var md = tagMetadataService.find(f.getTag());
                                return new TagFieldDTO(
                                        f.getTag(),
                                        f.getValue(),
                                        md.getHint(),
                                        md.getExample()
                                );
                            }).collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("fileUploaded", true);
            redirectAttributes.addFlashAttribute("fields", fields);
            redirectAttributes.addFlashAttribute("tags", tagMap.keySet());
            redirectAttributes.addFlashAttribute("tagMap", tagMap);
            redirectAttributes.addFlashAttribute("successMessage", "Импорт успешно выполнен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Не удалось импортировать CSV: " + e.getMessage());
        }
        return "redirect:/upload";
    }
}
