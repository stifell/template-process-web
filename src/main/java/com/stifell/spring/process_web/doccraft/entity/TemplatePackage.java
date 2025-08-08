package com.stifell.spring.process_web.doccraft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stifell on 05.08.2025
 */
@Entity
@Table(name = "template_packages")
public class TemplatePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "templatePackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageFile> files = new ArrayList<>();

    public TemplatePackage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PackageFile> getFiles() {
        return files;
    }

    public void setFiles(List<PackageFile> files) {
        this.files = files;
    }
}
