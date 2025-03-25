package com.enigmacamp.utils.validate_request;

import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.utils.EntityValidation;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Configuration
public class MountainValidation implements EntityValidation<MountainRequest> {
    @Override
    public void validateCreateRequest(MountainRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain name should not be empty");
        }
        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain location should not be empty");
        }
        if (request.getStatus() == null || request.getStatus().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain status should not be empty");
        }
        if (request.getPrice() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain price should not be empty");
        }
        if (request.getPrice().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain price should not be less than 1");
        }
        if (request.getToilet() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain toilet should not be empty! [true or false]");
        }
        if (request.getWater() == null || request.getWater().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain water should not be empty! [true or false]");
        }
        if (request.getQuotaLimit() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain quota limit should not be empty");
        }
        if (request.getIsOpen() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain isOpen should not be empty! [true or false]");
        }
        if (request.getAssignedRanger() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain assigned ranger should not be empty");
        }
    }

    @Override
    public void validateUpdateRequest(MountainRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain id should not be empty");
        }
        if (request.getName() != null) {
            if (request.getName().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain name should not be empty");
            }
        }
        if (request.getLocation() != null) {
            if (request.getLocation().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain location should not be empty");
            }
        }
        if (request.getStatus() != null) {
            if (request.getStatus().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain status should not be empty");
            }
        }
        if (request.getPrice() != null) {
            if (request.getPrice().toString().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain price should not be empty");
            }
            if (request.getPrice().compareTo(BigDecimal.ZERO) < 1 ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain price should not be less than 1");
            }
        }
        if (request.getToilet() != null) {
            if (request.getToilet().toString().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain toilet should not be empty! [true or false]");
            }
        }
        if (request.getWater() != null) {
            if (request.getWater().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain water should not be empty! [true or false]");
            }
        }
        if (request.getQuotaLimit() != null) {
            if (request.getQuotaLimit().toString().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain quota limit should not be empty");
            }
        }
        if (request.getIsOpen() != null) {
            if (request.getIsOpen().toString().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mountain isOpen should not be empty! [true or false]");
            }
        }
    }
}
