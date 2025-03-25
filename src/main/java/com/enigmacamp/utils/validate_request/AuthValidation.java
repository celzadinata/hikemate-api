package com.enigmacamp.utils.validate_request;

import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.utils.EntityValidation;
import com.enigmacamp.utils.UtilityTool;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class AuthValidation implements EntityValidation<NewUserRequest> {
    @Override
    public void validateCreateRequest(NewUserRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name should not be empty");
        }
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User phone should not be empty");
        }
        validatePhoneNumber(request);
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password should not be empty");
        }
        if (request.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password must be contain 8 or more characters");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email should not be empty");
        }
        validateEmail(request);
    }



    @Override
    public void validateUpdateRequest(NewUserRequest request) {

    }

    private static void validateEmail(NewUserRequest request) {
        if (!UtilityTool.validate(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email must be in correct format");
        }
    }

    private void validatePhoneNumber(NewUserRequest request) {
        if (!UtilityTool.isDigit(request.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User phone could not contain character");
        }
        if (request.getPhone().length() < 10 || request.getPhone().length() > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User phone must be contain 10-14 digits");
        }
        if (!request.getPhone().startsWith("0")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User phone must be start with 0");
        }
    }
}
