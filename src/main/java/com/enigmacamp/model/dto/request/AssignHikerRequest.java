package com.enigmacamp.model.dto.request;

import com.enigmacamp.model.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignHikerRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
