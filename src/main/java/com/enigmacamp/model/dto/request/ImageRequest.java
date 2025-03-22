package com.enigmacamp.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    private String id;
    private String name;
    private String path;
    private Long size;
    private String contentType;
}
