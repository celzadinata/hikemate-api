package com.enigmacamp.model.dto.request.payment.advanced_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentPageExpiryRequest {

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("unit")
    private String unit;
}
