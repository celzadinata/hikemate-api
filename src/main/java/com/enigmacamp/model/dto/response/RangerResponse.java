package com.enigmacamp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RangerResponse {
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MountainResponse mountainResponse;
    private String name;
    private String phoneNumber;
    private Timestamp assignedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
