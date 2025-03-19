package com.enigmacamp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("redirect_url")
    private String redirectUrl;
}
