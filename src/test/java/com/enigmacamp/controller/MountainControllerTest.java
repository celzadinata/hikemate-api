package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.response.JwtClaims;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.security.AccessDeniedHandlerImpl;
import com.enigmacamp.security.AuthenticationEntryPointImpl;
import com.enigmacamp.security.AuthenticationFilter;
import com.enigmacamp.security.SecurityConfiguration;
import com.enigmacamp.service.JwtService;
import com.enigmacamp.service.MountainService;
import com.enigmacamp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Import({AccessDeniedHandlerImpl.class, AuthenticationFilter.class, SecurityConfiguration.class, AuthenticationEntryPointImpl.class})
@WebMvcTest(MountainController.class)
class MountainControllerTest {
    @MockitoBean
    private MountainService mountainService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MountainRequest request;
    MountainResponse response;
    Mountain mountain1;
    Mountain mountain2;
    Mountain mountain3;
    JwtClaims jwtClaims;
    UserAccount userAccount;
    String token;

    @BeforeEach
    void setUp(){
        request = MountainRequest.builder()
                .id("1")
                .name("mountain_jpa test")
                .description("mountain_jpa description test")
                .price(new BigDecimal(12000))
                .status(MountainStatus.OPEN.toString())
                .water("Tersedia di beberapa tempat")
                .toilet(true)
                .isOpen(true)
                .quotaLimit(130)
                .location("Jakarta Utara")
                .build();

        mountain1 = Mountain.builder()
                .id("1")
                .name("mountain_jpa test")
                .description("mountain_jpa description test")
                .price(new BigDecimal(12000))
                .status(MountainStatus.OPEN)
                .water("Tersedia di beberapa tempat")
                .toilet(true)
                .isOpen(true)
                .quotaLimit(130)
                .location("Jakarta Utara")
                .build();

        jwtClaims = JwtClaims.builder()
                .userAccountId("c4fa0582-73df-4109-850f-214d030b6f17")
                .roles(List.of("SUPERADMIN"))
                .build();

        userAccount = UserAccount.builder()
                .id(jwtClaims.getUserAccountId())
                .email("email@email.com")
                .password("password")
                .role(jwtClaims.getRoles().stream().map(s -> Role.builder()
                                .id("1")
                                .role(UserRole.valueOf(s))
                                .build())
                        .toList())
                .build();

        token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCIsInVzZXJBY2NvdW50SWQiOiJjNGZhMDU4Mi03M2RmLTQxMDktODUwZi0yMTRkMDMwYjZmMTciLCJyb2xlcyI6WyJDVVNUT01FUiJdLCJpYXQiOjE3MzkzNTI5ODMsImV4cCI6MTczOTM1NjU4M30.SvIKHRkGGmDNQCgyI1hcbg-5mTsE1e24RV0VwvY6ZRo";


//        mountain2 = Mountain.builder()
//                .id("2")
//                .name("Tango tset")
//                .description("Tango description test")
//                .price(20000L)
//                .stock(30)
//                .image(new Image())
//                .build();
//
//        mountain3 = Mountain.builder()
//                .id("3")
//                .name("My Roti test")
//                .description("My Roti description test")
//                .price(16499L)
//                .stock(6)
//                .image(new Image())
//                .build();
//
//        response = MountainResponse.builder()
//                .id(request.getId())
//                .name(request.getName())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .stock(request.getStock())
//                .build();
    }

    @Test
    void testAddNewMountainSuccess() throws Exception{
        when(jwtService.verifyJwtToken(any(String.class))).thenReturn(true);
        when(jwtService.getClaimsByToken(any(String.class))).thenReturn(jwtClaims);
        when(userService.loadUserById(jwtClaims.getUserAccountId())).thenReturn(userAccount);
        when(mountainService.create(request)).thenReturn(response);

        MockMultipartFile mountain
                = new MockMultipartFile(
                "mountain",
                "mountain.json",
                MediaType.APPLICATION_JSON_VALUE,
                request.toString().getBytes()
        );
        mockMvc.perform(
                multipart(HttpMethod.POST,APIUrl.MOUNTAIN_API)
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(objectMapper.writeValueAsBytes(request))
                        .content(objectMapper.writeValueAsBytes(request))
        ).andExpect(status().isCreated());
    }

//    @Test
//    void testGetAllMountainSucces() throws Exception{
//        Page<MountainResponse> responses = new PageImpl<>(List.of(response));
//        when(mountainService.getAll(any())).thenReturn(responses);
//
//        mockMvc.perform(
//                get(APIUrl.PRODUCT_API)
//        ).andExpect(status().isOk());
//    }
}