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
public class PaymentItemDetailsRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("name")
    private String name;
}
