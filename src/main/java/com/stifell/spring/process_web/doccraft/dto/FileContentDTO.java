package com.stifell.spring.process_web.doccraft.dto;

/**
 * @author stifell on 02.06.2025
 */
public class FileContentDTO {
    private final String fileName;
    private final byte[] content;

    public FileContentDTO(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }
}
