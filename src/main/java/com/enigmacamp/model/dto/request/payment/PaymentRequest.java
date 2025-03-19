package com.enigmacamp.model.dto.request.payment;

import com.enigmacamp.model.dto.request.payment.advanced_request.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @JsonProperty("transaction_details")
    private PaymentDetailRequest transactionDetails;

    @JsonProperty("item_details")
    private List<PaymentItemDetailsRequest> itemDetails;

    @JsonProperty("customer_details")
    private PaymentCustomerDetailRequest customerDetails;

    @JsonProperty("page_expiry")
    private PaymentPageExpiryRequest pageExpiry;

    @JsonProperty("shipping_address")
    private PaymentShippingAddressRequest shippingAddress;
//
//    @JsonProperty("transaction_details")
//    private PaymentDetailRequest transactionDetails;
//
//    @JsonProperty("transaction_details")
//    private PaymentDetailRequest transactionDetails;
}
