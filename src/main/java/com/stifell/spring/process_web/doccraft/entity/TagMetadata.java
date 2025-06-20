package com.stifell.spring.process_web.doccraft.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author stifell on 20.06.2025
 */
@Entity
@Table(name = "tag_metadata")
public class TagMetadata {
    @Id
    private String tag;
    @Column(columnDefinition = "TEXT")
    private String hint;
    @Column(columnDefinition = "TEXT")
    private String example;

    public TagMetadata() {
    }

    public TagMetadata(String tag, String hint, String example) {
        this.tag = tag;
        this.hint = hint;
        this.example = example;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
