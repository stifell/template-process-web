package com.stifell.spring.process_web.doccraft.dto;

import com.stifell.spring.process_web.doccraft.model.TagMap;

import java.util.List;

/**
 * @author stifell on 02.06.2025
 */
public class GenerationRequestDTO {
    private TagMap tagMap;
    private List<FileUploadDTO> files;

    public GenerationRequestDTO(TagMap tagMap, List<FileUploadDTO> files) {
        this.tagMap = tagMap;
        this.files = files;
    }

    public TagMap getTagMap() {
        return tagMap;
    }

    public void setTagMap(TagMap tagMap) {
        this.tagMap = tagMap;
    }

    public List<FileUploadDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }
}
