package com.stifell.spring.process_web.doccraft.dto;

/**
 * @author stifell on 20.06.2025
 */
public class TagFieldDTO {
    private String tag;
    private String value;
    private String hint;
    private String example;

    public TagFieldDTO(String tag, String value, String hint, String example) {
        this.tag = tag;
        this.value = value;
        this.hint = hint;
        this.example = example;
    }

    public TagFieldDTO(String tag, String value) {
        this.tag = tag;
        this.value = value;
        this.hint = "";
        this.example = "";
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
