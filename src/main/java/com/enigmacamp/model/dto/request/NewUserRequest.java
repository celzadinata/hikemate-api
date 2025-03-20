package com.enigmacamp.model.dto.request;

import com.enigmacamp.model.entity.Image;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile image;
}
