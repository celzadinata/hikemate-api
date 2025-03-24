package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.entity.*;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.service.*;
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

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RouteService routeService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Mountain mountain = mountainService.getByIdEntity(request.getMountainId());
        Ranger ranger = rangerService.getByMountainIdEntity(mountain);
        Hiker hiker = hikerService.getByIdEntity(request.getHikerId());
        Route route = routeService.getByIdEntity(request.getRouteId());
        Transaction newTransaction = transactionMapper.requestToEntity(request);
        newTransaction.setMountain(mountain);
        newTransaction.setRanger(ranger);
        newTransaction.setHiker(hiker);
        newTransaction.setTransactionDate(currentTimeStamp);
        newTransaction.setCreatedAt(currentTimeStamp);
        newTransaction.setUpdatedAt(currentTimeStamp);
        newTransaction.setPrice(mountain.getPrice());
        newTransaction.setRoute(route);

        if (checkIfMountainQuotaFull(request, mountain)) {
            throw new RuntimeException("Mountain limit on that day already reach maximum capacity");
        }

        transactionRepository.save(newTransaction);
        Payment payment = paymentService.createPayment(newTransaction);
        newTransaction.setPayment(payment);

        return transactionMapper.entityToResponse(newTransaction);
    }

    @Override
    public Page<TransactionResponse> getAll(Boolean isUp, Boolean isDown, String status, String rangerId, String hikerId, String mountainId, String hikerName, SearchRequest searchRequest) {
        Ranger ranger = findRangerById(rangerId);
        Hiker hiker = findHikerById(hikerId);
        Mountain mountain = findMountainById(mountainId);

        TransactionSpecification specification = new TransactionSpecification(isUp, isDown, status, ranger, hiker, mountain, hikerName);
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
        }else if (transactionFound.getIsUp() && !transactionFound.getIsDown()) {
            transactionFound.setIsDown(true);
            transactionFound.setUpdatedAt(currentTimeStamp);
        }

        return transactionMapper.entityToResponse(transactionRepository.saveAndFlush(transactionFound));
    }

    @Override
    public Page<TransactionResponse> getTransactionByMonthAndYear(Integer month, Integer year, String mountainId, SearchRequest searchRequest) {
        Sort.Direction sortDirection = searchRequest.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sortDirection, searchRequest.getSortBy());
        return transactionRepository.getTransactionByMonthAndYearAndMountain(month, year, mountainId, pageable).map(transactionMapper::entityToResponse);
    }

    @Override
    public Transaction getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private Transaction findByIdOrThrowNotFound(String id){
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found!", new RuntimeException("Transaction not found!", new Throwable())));
    }

    private Boolean checkIfMountainQuotaFull(TransactionRequest request, Mountain mountain){
        String startDate = request.getStartDate().substring(0, 10);
        System.out.println(startDate);

        Integer startDateCount = transactionRepository.countTotalTransactionByStartDateAndIsUp(startDate, mountain.getId());
        Integer endDateCount = transactionRepository.countTotalTransactionByEndDateAndIsUp(startDate, mountain.getId());
        System.out.println("Start date count: " + startDateCount);
        System.out.println("End date count: " + endDateCount);
        System.out.println("Mountain quota: " + mountain.getQuotaLimit());

        return startDateCount + endDateCount >= mountain.getQuotaLimit();
    }

    private Ranger findRangerById(String rangerId){
        if (rangerId != null && !rangerId.isEmpty()) {
            return rangerService.getByIdEntity(rangerId);
        } else {
            return null;
        }
    }
    private Hiker findHikerById(String hikerId){
        if (hikerId != null && !hikerId.isEmpty()) {
            return hikerService.getByIdEntity(hikerId);
        } else {
            return null;
        }
    }
    private Mountain findMountainById(String mountainId){
        if (mountainId != null && !mountainId.isEmpty()) {
            return mountainService.getByIdEntity(mountainId);
        } else {
            return null;
        }
    }
}
