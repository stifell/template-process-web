package com.stifell.spring.process_web.doccraft.processor;

import com.stifell.spring.process_web.doccraft.model.TagMap;

import java.io.File;
import java.io.IOException;

/**
 * @author stifell on 28.05.2025
 */
public abstract class WordProcessor<T> {
    protected TagMap tagMap;
    protected File file;

    WordProcessor(TagMap tagMap, File file) {
        this.tagMap = tagMap;
        this.file = file;
    }

    /**
     * Реализация изменения файла.
     */
    protected abstract void changeFile(String newFilePath) throws IOException;

    /**
     * Выполняет замену текста в документе.
     */
    protected abstract T replaceText(T document);

    /**
     * Сохраняет документ в указанный файл.
     */
    protected abstract void saveFile(String filePath, T document) throws IOException;
}
