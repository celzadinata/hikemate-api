package com.enigmacamp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MountainResponse {
    private String id;
    private String name;
    private String location;
    private String status;
    private BigDecimal price;
    private String description;
    private Boolean toilet;
    private String water;
    private Integer quotaLimit;
    private Boolean isOpen;
    private String mountainCoverUrl;
    private List<String> baseCampImagesUrl;
    private List<MountainRouteResponse> mountainRoutes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RangerResponse rangerResponse;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
