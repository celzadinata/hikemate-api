package com.enigmacamp.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MountainRequest {

    private String name;
    private String location;
    private String status;
    private BigDecimal price;
}
