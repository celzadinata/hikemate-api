package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.TRANSACTIONS_DETAILS)
public class TransactionDetail extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT CHECK(quantity > 0)")
    private Integer quantity;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT CHECK(price > 0)")
    private BigDecimal price;

    @Column(name = "subtotal", nullable = false, columnDefinition = "BIGINT CHECK(subtotal > 0)")
    private BigDecimal subtotal;
}
