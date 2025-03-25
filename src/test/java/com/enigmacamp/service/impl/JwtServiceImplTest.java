package com.enigmacamp.service.impl;

import com.auth0.jwt.JWTVerifier;
import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.dto.response.JwtClaims;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private HikerService hikerService;
    @Mock
    private RangerService rangerService;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJoaWtlbWF0ZSIsInVzZXJBY2NvdW50SWQiOiJpZCIsInVzZXJMb2dnZWRJbklkIjoiaWQiLCJuYW1lIjoibmFtZSIsInJvbGVzIjpbIkhJS0VSIl0sImlhdCI6MTc0Mjg4MjYzOCwiZXhwIjoxNzQyODg2MjM4fQ.5LG3-TWHpfi3n-7M_4rEmWO51LQbRU7mMm25hVXmvbE";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "issuer", "hikemate");
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "hikemate123");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 3600);
    }

    @Test
    void testGenerateToken() {
        final UserAccount userAccount = UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build();

        final Ranger ranger = Ranger.builder()
                .id("id")
                .name("name")
                .build();
        when(rangerService.getByUserAccountEntity(UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn(ranger);

        final Hiker hiker = Hiker.builder()
                .id("id")
                .name("name")
                .build();
        when(hikerService.getByUserAccountEntity(UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn(hiker);

        final UserAccount userAccount1 = UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build();
        when(userService.loadUserById("id")).thenReturn(userAccount1);

        final String result = jwtService.generateToken(userAccount);

        assertThat(result).isEqualTo(TEST_TOKEN);
    }

    @Test
    void testRefreshToken() {
        final UserAccount userAccount = UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build();
        when(userService.loadUserById("userAccountId")).thenReturn(userAccount);

        final Ranger ranger = Ranger.builder()
                .id("id")
                .name("name")
                .build();
        when(rangerService.getByUserAccountEntity(UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn(ranger);

        final Hiker hiker = Hiker.builder()
                .id("id")
                .name("name")
                .build();
        when(hikerService.getByUserAccountEntity(UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn(hiker);

        final String result = jwtService.refreshToken("Bearer " + TEST_TOKEN);

        assertThat(result).isEqualTo(TEST_TOKEN);
    }

    @Test
    void testVerifyJwtToken() {
        assertThat(jwtService.verifyJwtToken("Bearer " + TEST_TOKEN)).isTrue();
    }

    @Test
    void testGetClaimsByToken() {
        final JwtClaims result = jwtService.getClaimsByToken("Bearer " + TEST_TOKEN);
    }

    @Test
    void testParseJwt() {
        assertThat(jwtService.parseJwt("Bearer " + TEST_TOKEN)).isEqualTo(TEST_TOKEN);
    }

    @Test
    void testGetVerifier() {
        final JWTVerifier result = jwtService.getVerifier("jwtSecret", "issuer");
    }
}
