package com.stifell.spring.process_web.doccraft.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @author stifell on 13.08.2025
 */
@Entity
@Table(name = "generation_history")
public class GenerationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "package_id")
    private Long packageId;

    @CreationTimestamp
    @Column(name = "generation_date", nullable = false, updatable = false)
    private LocalDateTime generationDate;

    @Column(name = "author_count", nullable = false)
    private int authorCount;

    @Lob
    @Column(name = "file_names", columnDefinition = "LONGTEXT", nullable = false)
    private String fileNames;

    @Lob
    @Column(name = "tag_map_json", columnDefinition = "LONGTEXT", nullable = false)
    private String tagMapJson;


    public GenerationHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(int authorCount) {
        this.authorCount = authorCount;
    }

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public String getTagMapJson() {
        return tagMapJson;
    }

    public void setTagMapJson(String tagMapJson) {
        this.tagMapJson = tagMapJson;
    }
}
