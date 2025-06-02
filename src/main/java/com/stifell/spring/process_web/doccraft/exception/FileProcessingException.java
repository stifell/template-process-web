package com.stifell.spring.process_web.doccraft.exception;

/**
 * @author stifell on 02.06.2025
 */
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
