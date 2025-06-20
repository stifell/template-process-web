package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.entity.TagMetadata;
import com.stifell.spring.process_web.doccraft.repository.TagMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stifell on 20.06.2025
 */
@Service
public class TagMetadataService {
    @Autowired
    private TagMetadataRepository tagMetadataRepository;

    public TagMetadata find(String tag) {
        return tagMetadataRepository.findById(tag).orElse(new TagMetadata(tag, "", ""));
    }
}
