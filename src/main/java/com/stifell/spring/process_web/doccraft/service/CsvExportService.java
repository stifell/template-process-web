package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author stifell on 04.07.2025
 */
@Service
public class CsvExportService {
    @Autowired
    private TagMetadataService tagMetadataService;

    public ByteArrayResource exportyTagsAsCsv(TagMap tagMap) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                 CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                         .withDelimiter(';')
                         .withHeader("tag", "hint", "example", "value"))) {

                for (Map.Entry<String, String> e : tagMap.entrySet()) {
                    var md = tagMetadataService.find(e.getKey());
                    printer.printRecord(
                            e.getKey(),
                            md.getHint(),
                            md.getExample(),
                            e.getValue()
                    );
                }
            }

            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка генерации CSV", e);
        }
    }
}
