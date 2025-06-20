package com.stifell.spring.process_web.doccraft.dto;

/**
 * @author stifell on 02.06.2025
 */
public class FileContentDTO {
    private String fileName;
    private byte[] content;

    public FileContentDTO(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

}
