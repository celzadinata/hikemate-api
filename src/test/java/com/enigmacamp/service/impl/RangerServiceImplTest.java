package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.RangerRepository;
import com.enigmacamp.utils.exception.ResourceNotFoundException;
import com.enigmacamp.utils.mapper.RangerMapper;
import com.enigmacamp.utils.validate_request.AuthValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RangerServiceImplTest {

    @Mock
    private RangerRepository rangerRepository;
    @Mock
    private RangerMapper ranggerMapper;
    @InjectMocks
    private RangerServiceImpl rangerService;

    @Test
    void testCreate() {
        RangerRequest request = RangerRequest.builder()
                .name("name")
                .phoneNumber("phoneNumber")
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();
        RangerResponse expectedResult = RangerResponse.builder().build();

        Ranger ranger = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();
        when(ranggerMapper.requestToEntity(RangerRequest.builder()
                .name("name")
                .phoneNumber("phoneNumber")
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(ranger);

        Ranger ranger1 = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();
        when(rangerRepository.save(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(ranger1);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        RangerResponse result = rangerService.create(request);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllRangers() {
        SearchRequest pageable = SearchRequest.builder()
                .query("query")
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("id")
                .build();

        Page<Ranger> rangers = new PageImpl<>(List.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build()));
        when(rangerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rangers);

        Page<Ranger> rangers1 = new PageImpl<>(List.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build()));
        when(rangerRepository.findAll(any(Pageable.class))).thenReturn(rangers1);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        Page<RangerResponse> result = rangerService.getAllRangers(pageable);

    }

    @Test
    void testGetById() {
        RangerResponse expectedResult = RangerResponse.builder().build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findById("id")).thenReturn(ranger);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        RangerResponse result = rangerService.getById("id");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdateRanger() {
        RangerRequest request = RangerRequest.builder()
                .name("name")
                .phoneNumber("phoneNumber")
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();
        RangerResponse expectedResult = RangerResponse.builder().build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findById("id")).thenReturn(ranger);

        Ranger ranger1 = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();
        when(rangerRepository.save(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(ranger1);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        RangerResponse result = rangerService.updateRanger(request);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testDelete() {
        RangerResponse expectedResult = RangerResponse.builder().build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findById("id")).thenReturn(ranger);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        RangerResponse result = rangerService.delete("id");

        assertThat(result).isEqualTo(expectedResult);
        verify(rangerRepository).saveAndFlush(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
    }

    @Test
    void testGetByIdEntity() {
        Ranger expectedResult = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findById("id")).thenReturn(ranger);

        Ranger result = rangerService.getByIdEntity("id");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByUserAccountEntity() {
        UserAccount userAccount = UserAccount.builder()
                .id("id")
                .build();
        Ranger expectedResult = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findRangerByUserAccount(UserAccount.builder()
                .id("id")
                .build())).thenReturn(ranger);

        Ranger result = rangerService.getByUserAccountEntity(userAccount);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByMountainId() {
        Mountain mountain = Mountain.builder().build();
        RangerResponse expectedResult = RangerResponse.builder().build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findByMountain(Mountain.builder().build())).thenReturn(ranger);

        when(ranggerMapper.entityToResponse(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());

        RangerResponse result = rangerService.getByMountainId(mountain);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByMountainIdEntity() {
        Mountain mountain = Mountain.builder().build();
        Ranger expectedResult = Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build();

        Optional<Ranger> ranger = Optional.of(Ranger.builder()
                .id("id")
                .name("name")
                .phoneNumber("phoneNumber")
                .assignedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .userAccount(UserAccount.builder()
                        .id("id")
                        .build())
                .build());
        when(rangerRepository.findByMountain(Mountain.builder().build())).thenReturn(ranger);

        Ranger result = rangerService.getByMountainIdEntity(mountain);

        assertThat(result).isEqualTo(expectedResult);
    }
}
