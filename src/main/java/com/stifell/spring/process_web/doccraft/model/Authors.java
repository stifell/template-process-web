package com.stifell.spring.process_web.doccraft.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stifell on 28.05.2025
 */
public class Authors {
    private List<TagMap> tagAuthors;
    private int authorCount;

    public Authors(int authorCount) {
        this.authorCount = authorCount;
        tagAuthors = new ArrayList<>();
        for (int i = 0; i < authorCount; i++) {
            tagAuthors.add(new TagMap());
        }
    }

    // Метод для получения тегов первого автора (основной TagMap)
    public TagMap getMainTagMap() {
        if (tagAuthors == null || tagAuthors.isEmpty()) {
            return new TagMap();
        }
        return tagAuthors.get(0);
    }

    // Метод для получения тегов автора по индексу
    public TagMap getTagMapByIndex(int index) {
        if (tagAuthors == null || tagAuthors.size() <= index) {
            return new TagMap();
        }
        return tagAuthors.get(index);
    }

    public List<TagMap> getTagMaps() {
        return tagAuthors;
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public void addTagToAuthor(int authorIndex, String tag, String value) {
        if (authorIndex >= 0 && authorIndex < tagAuthors.size()) {
            tagAuthors.get(authorIndex).addTag(tag, value);
        }
    }
}
