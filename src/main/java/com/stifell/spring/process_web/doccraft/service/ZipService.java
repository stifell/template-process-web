package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.ProcessedDocumentDTO;
import com.stifell.spring.process_web.doccraft.exception.ZipCreationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author stifell on 02.06.2025
 */
@Service
public class ZipService {
    public Resource createZipArchive(List<ProcessedDocumentDTO> documents) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (ProcessedDocumentDTO doc : documents) {
                ZipEntry entry = new ZipEntry(doc.getFileName());
                zos.putNextEntry(entry);
                zos.write(doc.getContent());
                zos.closeEntry();
            }
            zos.finish();

            return new ByteArrayResource(baos.toByteArray());
        } catch (IOException e) {
            throw new ZipCreationException("ZIP archive creation error", e);
        }
    }
}
