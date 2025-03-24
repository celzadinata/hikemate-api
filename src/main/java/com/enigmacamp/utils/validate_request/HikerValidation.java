package com.enigmacamp.utils.validate_request;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.utils.EntityValidation;
import com.enigmacamp.utils.UtilityTool;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class HikerValidation implements EntityValidation<HikerRequest> {
    private static final int MIN_PHONE_LENGTH = 10;
    private static final int MAX_PHONE_LENGTH = 14;

    @Override
    public void validateCreateRequest(HikerRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        } else {
            validateEmail(request.getEmail());
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number cannot be empty");
        } else {
            validatePhoneNumber(request.getPhoneNumber());
        }
    }

    @Override
    public void validateUpdateRequest(HikerRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be empty for update");
        }

        if (request.getName() != null && request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be empty string");
        }

        if (request.getEmail() != null) {
            if (request.getEmail().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty string");
            } else {
                validateEmail(request.getEmail());
            }
        }

        if (request.getPassword() != null && request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty string");
        }

        if (request.getPhoneNumber() != null) {
            if (request.getPhoneNumber().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number cannot be empty string");
            } else {
                validatePhoneNumber(request.getPhoneNumber());
            }
        }
    }

    private void validateEmail(String email) {
        if (!UtilityTool.validate(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!UtilityTool.isDigit(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number should not contains character");
        }
        if (!phoneNumber.startsWith("0")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must start with 0");
        }
        if (phoneNumber.length() < MIN_PHONE_LENGTH || phoneNumber.length() > MAX_PHONE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be between " +
                    MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " characters");
        }
    }
}

