package com.stifell.spring.process_web.doccraft.exception;

/**
 * @author stifell on 02.06.2025
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
