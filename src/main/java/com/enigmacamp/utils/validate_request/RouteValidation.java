package com.enigmacamp.utils.validate_request;

import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.utils.EntityValidation;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class RouteValidation implements EntityValidation<RouteRequest> {
    @Override
    public void validateCreateRequest(RouteRequest request) {
        if (request.getRouteName() == null || request.getRouteName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route name should not be empty");
        }
    }

    @Override
    public void validateUpdateRequest(RouteRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route id should not be empty");
        }
        if (request.getRouteName() == null || request.getRouteName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route name should not be empty");
        }
    }
}
