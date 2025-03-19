package com.enigmacamp.model.dto.request;

import com.enigmacamp.model.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RangerRequest {
    private String id;
    private String name;
    private String phoneNumber;
    private UserAccount userAccount;
}
