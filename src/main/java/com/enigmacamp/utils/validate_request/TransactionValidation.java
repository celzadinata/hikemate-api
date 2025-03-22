package com.enigmacamp.utils.validate_request;

import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.utils.EntityValidation;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionValidation implements EntityValidation<TransactionRequest> {
    @Override
    public void validateCreateRequest(TransactionRequest request) {

    }

    @Override
    public void validateUpdateRequest(TransactionRequest request) {

    }
}
