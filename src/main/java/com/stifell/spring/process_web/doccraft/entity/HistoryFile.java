package com.stifell.spring.process_web.doccraft.entity;

import jakarta.persistence.*;

/**
 * @author stifell on 13.08.2025
 */
@Entity
@Table(name = "history_files")
public class HistoryFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private GenerationHistory history;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] content;

    public HistoryFile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenerationHistory getHistory() {
        return history;
    }

    public void setHistory(GenerationHistory history) {
        this.history = history;
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
}
