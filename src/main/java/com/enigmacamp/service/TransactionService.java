package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getAll(Boolean isUp, Boolean isDown, String status, SearchRequest searchRequest);
    TransactionResponse getById(String id);
    TransactionResponse updateHikerStatus(String id);
}
