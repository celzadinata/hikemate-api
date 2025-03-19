package com.enigmacamp.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequest {
    private String search;
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
}
