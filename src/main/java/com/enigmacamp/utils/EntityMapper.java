package com.enigmacamp.utils;

public interface EntityMapper<E,T, R> {
    R entityToResponse (E entity);
    T responseToRequest (R response);
    E requestToEntity (T request);
}
