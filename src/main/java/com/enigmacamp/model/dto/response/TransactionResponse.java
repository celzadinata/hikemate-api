package com.enigmacamp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String transactionId;
    private BigDecimal price;
    private Timestamp transactionDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private Boolean isUp;
    private Boolean isDown;
    private String qrCodeUrl;
    private String paymentStatus;
    private HikerResponse hiker;
    private RangerResponse ranger;
    private MountainResponse mountain;
}
