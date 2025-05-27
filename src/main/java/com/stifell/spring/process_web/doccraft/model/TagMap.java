package com.stifell.spring.process_web.doccraft.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author stifell on 28.05.2025
 * <p>
 * Класс TagMap представляет словарь тегов и соответствующих им значений.
 * Используется для хранения этих тегов.
 * Является оберткой HashMap.
 */
public class TagMap extends HashMap<String, String> {
    public TagMap() {
        super();
    }

    public TagMap copyTagMap() {
        return (TagMap) this.clone();
    }

    public void removeTag(String tag) {
        remove(tag);
    }

    // метод для добавления нового тега и его значения
    public void addTag(String tag, String value) {
        put(tag, value);
    }

    // метод для объединения двух TagMap
    public void combineTags(TagMap other) {
        this.putAll(other);
    }
}
