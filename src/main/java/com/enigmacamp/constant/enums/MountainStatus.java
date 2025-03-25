package com.enigmacamp.constant.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum MountainStatus {
    DANGEROUS,
    SAFE,
    RAINING,
    OPEN;

    public static MountainStatus findByName(String name) {
        for (MountainStatus status : values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status should be either [DANGEROUS|SAFE|RAINING|OPEN]", new Throwable());
    }
}
