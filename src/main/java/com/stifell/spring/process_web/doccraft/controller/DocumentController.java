package com.stifell.spring.process_web.doccraft.controller;

import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.processor.WordDOCX;
import com.stifell.spring.process_web.doccraft.service.WordProcessorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * @author stifell on 28.05.2025
 */
@Controller
public class DocumentController {
    @Autowired
    private WordProcessorService wordProcessorService;

    @GetMapping("/upload")
    public String getUploadPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "upload-page";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile[] files,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session,
                                   Model model) {
        if (files == null || files.length == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Выберите файл для загрузки.");
            return "redirect:/upload";
        }

        TagMap tagMap = new TagMap();
        Set<String> allTags = new HashSet<>();
        List<Map.Entry<String, String>> tempFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Выберите файлы для загрузки.");
                return "redirect:/upload";
            }

            String filename = file.getOriginalFilename();
            if (filename != null && !filename.endsWith(".docx")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Можно загружать только файлы с расширением .docx.");
                return "redirect:/upload";
            }

            // Логика сохранения файла
            try {
                File tempFile = File.createTempFile("uploaded-", ".docx");
                file.transferTo(tempFile);
                tempFiles.add(new AbstractMap.SimpleEntry<>(filename, tempFile.getAbsolutePath()));

                // Получение тегов из файла
                Map<String, String> tagsMap = wordProcessorService.writeTagsToSet(new File[]{tempFile});
                tagMap.putAll(tagsMap);
                allTags.addAll(tagsMap.keySet());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении файла: " + e.getMessage());
                return "redirect:/upload";
            }
        }
        session.setAttribute("tempFiles", tempFiles);
        session.setAttribute("tagMap", tagMap);

        model.addAttribute("files", files);
        model.addAttribute("tags", allTags);
        model.addAttribute("fileUploaded", true);
        model.addAttribute("tagMap", tagMap);

        return "upload-page";
    }

    @PostMapping("/generate")
    public void generateDocument(@RequestParam Map<String, String> formParams,
                                 HttpSession session,
                                 HttpServletResponse response) {
        try {
            TagMap tagMap = (TagMap) session.getAttribute("tagMap");
            List<Map.Entry<String, String>> tempFiles =
                    (List<Map.Entry<String, String>>) session.getAttribute("tempFiles");
            if (tempFiles == null || tempFiles.isEmpty()) {
                throw new IllegalArgumentException("Файлы не найдены.");
            }

            tagMap.replaceAll(formParams::getOrDefault);

            // Логика создания ZIP
            File zipFile = File.createTempFile("documents-", ".zip");
            try (ZipFile zip = new ZipFile(zipFile)) {
                ZipParameters params = new ZipParameters();
                params.setCompressionMethod(CompressionMethod.DEFLATE);
                params.setCompressionLevel(CompressionLevel.NORMAL);

                for (Map.Entry<String, String> entry : tempFiles) {
                    File sourceFile = new File(entry.getValue());
                    File modifiedFile = File.createTempFile("modified-", ".docx");
                    WordDOCX.createFile(tagMap, sourceFile, modifiedFile.getAbsolutePath());

                    params.setFileNameInZip(entry.getKey());
                    zip.addFile(modifiedFile, params);
                }
            }

            // Отправка архива
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=documents.zip");
            Files.copy(zipFile.toPath(), response.getOutputStream());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
