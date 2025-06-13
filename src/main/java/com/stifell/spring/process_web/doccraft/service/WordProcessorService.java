package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.model.TagMap;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author stifell on 28.05.2025
 */
@Service
public class WordProcessorService {
    private final String regex = "\\$\\{[^}]+\\}";

    public TagMap writeTagsToSet(File[] files) throws IOException {
        TagMap tagsMap = new TagMap();
        Pattern pattern = Pattern.compile(regex);
        for (File file : files) {
            if (file.isFile() && (file.getName().endsWith(".doc") || file.getName().endsWith(".docx"))) {
                String text = readTextFromFile(file);
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    String tag = matcher.group();
                    if (!tagsMap.containsKey(tag)) {
                        tagsMap.addTag(tag, "");
                    }
                }
            }
        }
        return tagsMap;
    }

    private String readTextFromFile(File file) throws IOException {
        if (file.getName().endsWith(".docx")) {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument doc = new XWPFDocument(fis);
                 XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
                return extractor.getText();
            }
        } else if (file.getName().endsWith(".doc")) {
            try (FileInputStream fis = new FileInputStream(file);
                 HWPFDocument doc = new HWPFDocument(fis);
                 WordExtractor extractor = new WordExtractor(doc)) {
                return extractor.getText();
            }
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }
}
