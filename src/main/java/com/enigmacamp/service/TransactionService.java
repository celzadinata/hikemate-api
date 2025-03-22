package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.entity.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getAll(Boolean isUp, Boolean isDown, String status, String rangerId, String hikerId, String mountainId, SearchRequest searchRequest);
    TransactionResponse getById(String id);
    TransactionResponse updateHikerStatus(String id);
    Page<TransactionResponse> getTransactionByMonthAndYear(Integer month, Integer year, String mountainId, SearchRequest searchRequest);

    Transaction getByIdEntity(String id);
}
