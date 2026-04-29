package com.oriontek.apiOriontek.utils;

@FunctionalInterface
public interface ToDTO<T> {
    T toDto();
}
