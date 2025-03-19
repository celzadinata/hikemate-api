package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.service.MountainService;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.service.TransactionService;
import com.enigmacamp.utils.mapper.TransactionMapper;
import com.enigmacamp.utils.specifications.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private HikerService hikerService;

    @Autowired
    private MountainService mountainService;

    @Autowired
    private RangerService rangerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Mountain mountain = mountainService.getByIdEntity(request.getMountainId());
        Ranger ranger = rangerService.getByIdEntity(request.getRangerId());
        Hiker hiker = hikerService.getByIdEntity(request.getHikerId());
        Transaction newTransaction = transactionMapper.requestToEntity(request);
        newTransaction.setMountain(mountain);
        newTransaction.setRanger(ranger);
        newTransaction.setHiker(hiker);
        newTransaction.setTransactionDate(currentTimeStamp);
        newTransaction.setCreatedAt(currentTimeStamp);
        newTransaction.setUpdatedAt(currentTimeStamp);
        newTransaction.setPrice(mountain.getPrice());

        return transactionMapper.entityToResponse(transactionRepository.save(newTransaction));
    }

    @Override
    public Page<TransactionResponse> getAll(Boolean isUp, Boolean isDown, String status, SearchRequest searchRequest) {
        TransactionSpecification specification = new TransactionSpecification(isUp, isDown, status);
        Sort.Direction sortDirection = searchRequest.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sortDirection, searchRequest.getSortBy());
        Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);
        return transactionPage.map(transactionMapper::entityToResponse);
    }

    @Override
    public TransactionResponse getById(String id) {
        return transactionMapper.entityToResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    public TransactionResponse updateHikerStatus(String id) {
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Transaction transactionFound = findByIdOrThrowNotFound(id);
        if (!transactionFound.getIsUp() && !transactionFound.getIsDown()) {
            transactionFound.setIsUp(true);
            transactionFound.setUpdatedAt(currentTimeStamp);
        }

        if (transactionFound.getIsUp() && !transactionFound.getIsDown()) {
            transactionFound.setIsDown(true);
            transactionFound.setUpdatedAt(currentTimeStamp);
        }

        return transactionMapper.entityToResponse(transactionRepository.saveAndFlush(transactionFound));
    }

    private Transaction findByIdOrThrowNotFound(String id){
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found!", new RuntimeException("Transaction not found!", new Throwable())));
    }
}
