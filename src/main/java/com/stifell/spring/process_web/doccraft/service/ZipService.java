package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.exception.ZipCreationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stifell on 02.06.2025
 */
@Slf4j
@Service
public class ZipService {
    private static final int BUFFER_SIZE = 8192;

    public Resource createZipArchive(List<FileContentDTO> documents) {
        log.debug("ZIP: starting archive for {} documents: {}",
                documents.size(),
                documents.stream().map(FileContentDTO::getFileName).toList());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            zos.setLevel(Deflater.BEST_SPEED);

            for (FileContentDTO doc : documents) {
                processDocumentForZip(doc, zos);
            }

            zos.finish();
            log.debug("ZIP: archive creation complete, total size = {} bytes", baos.size());
            return new ByteArrayResource(baos.toByteArray());

        } catch (IOException e) {
            log.error("ZIP: fatal error creating archive – {}", e.getMessage(), e);
            throw new ZipCreationException("ZIP archive creation error", e);
        }
    }

    private void processDocumentForZip(FileContentDTO doc, ZipOutputStream zos) throws IOException {
        String originalName = doc.getFileName();
        byte[] content = doc.getContent();

        if (originalName == null || originalName.trim().isEmpty()) {
            log.warn("Skipping document with empty file name");
            return;
        }

        if (content == null || content.length == 0) {
            log.warn("Skipping document '{}' due to empty content", originalName);
            return;
        }

        try {
            log.debug("Adding document '{}' to ZIP (size: {} bytes)", originalName, content.length);
            ZipEntry entry = new ZipEntry(originalName);
            zos.putNextEntry(entry);

            writeToZipWithBuffer(content, zos);

            zos.closeEntry();
        } catch (IOException e) {
            log.error("Failed to add document '{}' to ZIP: {}", originalName, e.getMessage(), e);
            // Продолжаем обработку остальных документов
        }
    }

    private String getStructuredPath(String fileName) {
        if (fileName.toLowerCase().endsWith(".pdf")) {
            return "PDF/" + fileName;
        } else if (fileName.toLowerCase().endsWith(".docx") ||
                fileName.toLowerCase().endsWith(".doc")) {
            return "Word/" + fileName;
        }
        return fileName;
    }

    private void writeToZipWithBuffer(byte[] content, ZipOutputStream zos) throws IOException {
        int offset = 0;
        while (offset < content.length) {
            int toWrite = Math.min(BUFFER_SIZE, content.length - offset);
            zos.write(content, offset, toWrite);
            offset += toWrite;
        }
    }
}
