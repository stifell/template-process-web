package com.stifell.spring.process_web.doccraft.exception;

/**
 * @author stifell on 02.06.2025
 */
public class InvalidFileTypeException extends FileProcessingException{
    public InvalidFileTypeException(String message) {
        super(message);
    }
}
