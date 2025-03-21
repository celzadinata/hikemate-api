package com.enigmacamp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    private String name;

    @JsonAlias("phone_number")
    private String phone;
    private String email;
    private String password;

    @JsonAlias("user_id")
    private String userId;
}
