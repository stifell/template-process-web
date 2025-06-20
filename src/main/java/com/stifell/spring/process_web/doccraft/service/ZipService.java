package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.FileContentDTO;
import com.stifell.spring.process_web.doccraft.exception.ZipCreationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stifell on 02.06.2025
 */
@Slf4j
@Service
public class ZipService {
    public Resource createZipArchive(List<FileContentDTO> documents) {
        log.debug("ZIP: starting archive for {} documents: {}",
                documents.size(),
                documents.stream().map(FileContentDTO::getFileName).toList());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (FileContentDTO doc : documents) {
                String name = doc.getFileName();
                byte[] content = doc.getContent();

                if (name == null || name.isBlank()) {
                    log.warn("ZIP: skipping entry with empty fileName");
                    continue;
                }
                if (content == null) {
                    log.warn("ZIP: skipping '{}' because content is null", name);
                    continue;
                }

                try {
                    log.debug("ZIP: adding entry '{}', size={} bytes", name, content.length);
                    ZipEntry entry = new ZipEntry(name);
                    zos.putNextEntry(entry);
                    zos.write(content);
                    zos.closeEntry();
                } catch (IOException e) {
                    log.error("ZIP: failed to add '{}' – {}", name, e.getMessage(), e);
                    // пропускаем этот файл, идём дальше
                }
            }

            zos.finish();
            log.debug("ZIP: archive creation complete, total size = {} bytes", baos.size());
            return new ByteArrayResource(baos.toByteArray());

        } catch (IOException e) {
            log.error("ZIP: fatal error creating archive – {}", e.getMessage(), e);
            throw new ZipCreationException("ZIP archive creation error", e);
        }
    }
}
