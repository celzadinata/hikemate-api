package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.TRANSACTIONS)
public class Transaction extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "hiker_id", nullable = true)
    private Hiker hiker;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ranger_pic_id", nullable = true)
    private Ranger ranger;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id", nullable = true)
    private Mountain mountain;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT CHECK(price > 0)")
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Column(name = "transaction_date", nullable = false)
    private Timestamp transactionDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "is_up", nullable = true)
    private Boolean isUp;

    @Column(name = "is_down", nullable = true)
    private Boolean isDown;

//    @Column(name = "qr_code_url", nullable = true)
//    private Image qrCode;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true, nullable = true)
    private Payment payment;
}
