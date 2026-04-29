package com.oriontek.apiOriontek.config;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}