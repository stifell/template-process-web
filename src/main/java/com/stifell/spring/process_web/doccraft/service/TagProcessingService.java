package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.model.Authors;
import com.stifell.spring.process_web.doccraft.model.TagMap;
import com.stifell.spring.process_web.doccraft.processor.MultiTagProcessor;
import com.stifell.spring.process_web.doccraft.processor.SharedTagProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stifell on 20.06.2025
 */
@Service
public class TagProcessingService {
    @Autowired
    private final MultiTagProcessor multiTagProcessor;
    private final SharedTagProcessor sharedTagProcessor;

    @Autowired
    public TagProcessingService(MultiTagProcessor multiTagProcessor, SharedTagProcessor sharedTagProcessor) {
        this.multiTagProcessor = multiTagProcessor;
        this.sharedTagProcessor = sharedTagProcessor;
    }

    public void processSpecialTags(TagMap tagMap, Authors authors, String fileType) {
        switch (fileType.toLowerCase()) {
            case "multi":
                multiTagProcessor.fillAuthorsTags(tagMap, authors);
                break;
            case "additional":
                sharedTagProcessor.fillAuthorsTags(tagMap, authors);
        }
    }
}
