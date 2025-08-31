package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author stifell on 01.09.2025
 */
@Service
public class PdfConversionService {
    private final OfficeManager officeManager;
    private final LocalConverter localConverter;

    @Autowired
    public PdfConversionService(OfficeManager officeManager, LocalConverter localConverter) {
        this.officeManager = officeManager;
        this.localConverter = localConverter;
    }

    @PostConstruct
    public void init() throws OfficeException {
        if (!officeManager.isRunning()) {
            officeManager.start();
        }
    }

    @PreDestroy
    public void destroy() throws OfficeException {
        if (officeManager.isRunning()) {
            officeManager.stop();
        }
    }

    public byte[] convertDocxToPdf(byte[] docxContent) throws OfficeException {
        try (InputStream inputStream = new ByteArrayInputStream(docxContent);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            localConverter.convert(inputStream)
                    .as(DefaultDocumentFormatRegistry.DOCX)
                    .to(outputStream)
                    .as(DefaultDocumentFormatRegistry.PDF)
                    .execute();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка конвертации DOCX в PDF", e);
        }
    }
}
