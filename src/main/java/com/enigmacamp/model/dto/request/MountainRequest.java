package com.enigmacamp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    @JsonAlias("assigned_ranger")
    private NewUserRequest assignedRanger;
    private MultipartFile image;
}
