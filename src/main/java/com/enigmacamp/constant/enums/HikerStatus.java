package com.enigmacamp.constant.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum HikerStatus {
    IDLE,
    CLIMBED,
    OFF,
    COMPLETED;

    public static HikerStatus findByName(String name) {
        for (HikerStatus status : values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status should be either [IDLE|CLIMBER|OFF|COMPLETED]", new Throwable());
    }
}
