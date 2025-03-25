package com.enigmacamp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MountainRequest {
    private String id;
    @NotEmpty(message = "Mountain name should not be empty")
    @NotNull(message = "Mountain name should not be empty")
    private String name;
    private String location;
    private String status;
    private BigDecimal price;
    private String description;
    private Boolean toilet;
    private String water;
    private Integer quotaLimit;

    @JsonAlias("is_open")
    private Boolean isOpen;

    @JsonAlias("assigned_ranger")
    private NewUserRequest assignedRanger;
    private MultipartFile image;
    private List<MultipartFile> baseCampImages;

    @JsonAlias("mountain_routes")
    private List<RouteRequest> mountainRoutes;
}
