package com.enigmacamp.utils;

public interface EntityValidation<T> {

    void validateCreateRequest(T request);
    void validateUpdateRequest(T request);
}
