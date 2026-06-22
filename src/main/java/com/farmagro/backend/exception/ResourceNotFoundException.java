package com.farmagro.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Integer id) {
        super("No se encontró " + resource + " con ID: " + id);
    }
}