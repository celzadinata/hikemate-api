package com.enigmacamp.model.dto.request;

import com.enigmacamp.model.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikerRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private UserAccount userAccount;
}
