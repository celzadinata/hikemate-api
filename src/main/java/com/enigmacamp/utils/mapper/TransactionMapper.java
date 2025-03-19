package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

@Configuration
public class TransactionMapper implements EntityMapper<Transaction, TransactionRequest, TransactionResponse> {

    @Autowired
    private HikerMapper hikerMapper;

    @Autowired
    private MountainMapper mountainMapper;

    @Autowired
    private RangerMapper rangerMapper;

    @Override
    public TransactionResponse entityToResponse(Transaction entity) {
        return TransactionResponse.builder()
                .transactionId(entity.getId())
                .price(entity.getPrice())
                .transactionDate(entity.getTransactionDate())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isUp(entity.getIsUp())
                .isDown(entity.getIsDown())
//                .qrCodeUrl(entity.getQrCode().getPath())
                .hiker(hikerMapper.entityToResponse(entity.getHiker()))
                .mountain(mountainMapper.entityToResponse(entity.getMountain()))
                .ranger(rangerMapper.entityToResponse(entity.getRanger()))
                .paymentStatus(entity.getPayment() != null? entity.getPayment().getTransactionStatus().name() : null)
                .paymentUrl(entity.getPayment() != null? entity.getPayment().getRedirectUrl() : null)
                .build();
    }

    @Override
    public TransactionRequest responseToRequest(TransactionResponse response) {
        return null;
    }

    @Override
    public Transaction requestToEntity(TransactionRequest request) {
        return Transaction.builder()
                .startDate(Timestamp.valueOf(request.getStartDate()))
                .endDate(Timestamp.valueOf(request.getEndDate()))
                .isUp(false)
                .isDown(false)
                .build();
    }
}
