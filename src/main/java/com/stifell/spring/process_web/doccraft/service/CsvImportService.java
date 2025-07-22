package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.dto.TagFieldDTO;
import com.stifell.spring.process_web.doccraft.exception.FileProcessingException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stifell on 05.07.2025
 */
@Service
public class CsvImportService {
    public List<TagFieldDTO> importFromCsv(MultipartFile file) {
        try (BOMInputStream bomIn = new BOMInputStream(file.getInputStream(), false, ByteOrderMark.UTF_8);
             Reader reader = new InputStreamReader(bomIn, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter(';')
                     .withFirstRecordAsHeader())) {

            List<TagFieldDTO> list = new ArrayList<>();
            for (CSVRecord r : parser) {
                String tag = r.get("tag");
                String value = r.get("value");
                list.add(new TagFieldDTO(tag, value));
            }
            return list;
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка чтения CSV", e);
        }
    }
}
