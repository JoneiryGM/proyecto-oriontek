package com.oriontek.apiOriontek.config;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con id " + id + " no encontrado");
    }
}
