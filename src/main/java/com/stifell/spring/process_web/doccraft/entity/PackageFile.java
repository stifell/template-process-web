package com.stifell.spring.process_web.doccraft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * @author stifell on 05.08.2025
 */
@Entity
@Table(name = "package_files")
public class PackageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fileName;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private TemplatePackage templatePackage;

    public PackageFile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public TemplatePackage getTemplatePackage() {
        return templatePackage;
    }

    public void setTemplatePackage(TemplatePackage templatePackage) {
        this.templatePackage = templatePackage;
    }
}
