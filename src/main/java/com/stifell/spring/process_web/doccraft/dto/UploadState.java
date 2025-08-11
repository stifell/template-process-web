package com.stifell.spring.process_web.doccraft.dto;

import java.util.List;

/**
 * @author stifell on 11.08.2025
 */
public class UploadState {
    private int authorCount;
    private Long packageId;
    private List<String> fileNames;
    private List<TagFieldDTO> fields;
    private boolean fileUploaded;

    public UploadState() {
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(int authorCount) {
        this.authorCount = authorCount;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<TagFieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<TagFieldDTO> fields) {
        this.fields = fields;
    }

    public boolean isFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(boolean fileUploaded) {
        this.fileUploaded = fileUploaded;
    }
}
