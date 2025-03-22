package com.enigmacamp.model.dto.request;

import com.enigmacamp.model.entity.UserAccount;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikerRequest {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private UserAccount userAccount;
    @JsonIgnore
    private MultipartFile ktpImage;
    @JsonIgnore
    private MultipartFile profilePicture;
}
