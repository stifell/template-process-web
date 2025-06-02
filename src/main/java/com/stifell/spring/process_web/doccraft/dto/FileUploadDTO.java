package com.stifell.spring.process_web.doccraft.dto;

/**
 * @author stifell on 02.06.2025
 */
public class FileUploadDTO {
    private final String originalName;
    private final byte[] content;

    public FileUploadDTO(String originalName, byte[] content) {
        this.originalName = originalName;
        this.content = content;
    }

    public String getOriginalName() {
        return originalName;
    }

    public byte[] getContent() {
        return content;
    }
}
