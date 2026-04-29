package com.oriontek.apiOriontek.utils;

@FunctionalInterface
public interface TransformFrom <F,T>{
    T from(F f);
}

