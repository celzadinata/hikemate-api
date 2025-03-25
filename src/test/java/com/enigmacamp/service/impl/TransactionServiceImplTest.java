package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.entity.*;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.service.*;
import com.enigmacamp.utils.mapper.TransactionMapper;
import com.enigmacamp.utils.specifications.TransactionSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private HikerService hikerService;
    @Mock
    private MountainService mountainService;
    @Mock
    private RangerService rangerService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RouteService routeService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testCreate() {
        TransactionRequest request = TransactionRequest.builder()
                .hikerId("hikerId")
                .mountainId("mountainId")
                .startDate("startDate")
                .routeId("routeId")
                .build();
        TransactionResponse expectedResult = TransactionResponse.builder().build();

        Mountain mountain = Mountain.builder()
                .id("id")
                .price(new BigDecimal("0.00"))
                .quotaLimit(0)
                .build();
        when(mountainService.getByIdEntity("mountainId")).thenReturn(mountain);

        when(rangerService.getByMountainIdEntity(Mountain.builder()
                .id("id")
                .price(new BigDecimal("0.00"))
                .quotaLimit(0)
                .build())).thenReturn(Ranger.builder().build());

        Hiker hiker = Hiker.builder()
                .ktp(Image.builder().build())
                .build();
        when(hikerService.getByIdEntity("hikerId")).thenReturn(hiker);

        when(routeService.getByIdEntity("routeId")).thenReturn(Route.builder().build());

        Transaction transaction = Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build();
        when(transactionMapper.requestToEntity(TransactionRequest.builder()
                .hikerId("hikerId")
                .mountainId("mountainId")
                .startDate("startDate")
                .routeId("routeId")
                .build())).thenReturn(transaction);

        when(transactionRepository.countTotalTransactionByStartDateAndIsUp("startDate", "id")).thenReturn(0);
        when(transactionRepository.countTotalTransactionByEndDateAndIsUp("endDate", "id")).thenReturn(0);
        when(paymentService.createPayment(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(Payment.builder().build());
        when(transactionMapper.entityToResponse(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(TransactionResponse.builder().build());

        TransactionResponse result = transactionService.create(request);

        assertThat(result).isEqualTo(expectedResult);
        verify(transactionRepository).save(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build());
    }

    @Test
    void testGetAll() {
        SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("id")
                .build();
        when(rangerService.getByIdEntity("rangerId")).thenReturn(Ranger.builder().build());

        Hiker hiker = Hiker.builder()
                .ktp(Image.builder().build())
                .build();
        when(hikerService.getByIdEntity("hikerId")).thenReturn(hiker);

        Mountain mountain = Mountain.builder()
                .id("id")
                .price(new BigDecimal("0.00"))
                .quotaLimit(0)
                .build();
        when(mountainService.getByIdEntity("mountainId")).thenReturn(mountain);

        Page<Transaction> transactions = new PageImpl<>(List.of(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build()));
        when(transactionRepository.findAll(any(TransactionSpecification.class), any(Pageable.class)))
                .thenReturn(transactions);

        when(transactionMapper.entityToResponse(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(TransactionResponse.builder().build());

        Page<TransactionResponse> result = transactionService.getAll(false, false, "status",
                "rangerId", "hikerId", "mountainId", "hikerName", searchRequest);

    }

    @Test
    void testGetById() {
        TransactionResponse expectedResult = TransactionResponse.builder().build();

        Optional<Transaction> transaction = Optional.of(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build());
        when(transactionRepository.findById("id")).thenReturn(transaction);

        when(transactionMapper.entityToResponse(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(TransactionResponse.builder().build());

        TransactionResponse result = transactionService.getById("id");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdateHikerStatus() {
        TransactionResponse expectedResult = TransactionResponse.builder().build();
        Image image = Image.builder()
                .id("id")
                .name("name")
                .path("path")
                .size(10L)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id("id")
                .role(List.of(Role.builder()
                        .id("id")
                        .role(UserRole.HIKER)
                        .build()))
                .build();

        Mountain mountain = Mountain.builder().build();
        Hiker hiker = Hiker.builder()
                .id("id")
                .email("email@email.com")
                .name("hiker")
                .profilePicture(image)
                .ktp(image)
                .userAccount(userAccount)
                .phoneNumber("09090999023")
                .ktp(Image.builder().build())
                .build();

        Ranger ranger = Ranger.builder()
                .id("id")
                .name("hiker")
                .userAccount(userAccount)
                .phoneNumber("09090999023")
                .assignedAt(new Timestamp(new Date().getTime()))
                .mountain(mountain)
                .build();

        Optional<Transaction> transaction = Optional.of(Transaction.builder()
                .id("id")
                .hiker(hiker)
                .ranger(ranger)
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build());
        when(transactionRepository.findById("id")).thenReturn(transaction);

        Transaction transaction1 = Transaction.builder()
                .id("id")
                .hiker(hiker)
                .ranger(ranger)
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build();
        when(transactionRepository.saveAndFlush(Transaction.builder()
                .id("id")
                .hiker(hiker)
                .ranger(ranger)
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(transaction1);

        when(transactionMapper.entityToResponse(Transaction.builder()
                .id("id")
                .hiker(hiker)
                .ranger(ranger)
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(TransactionResponse.builder().build());

        TransactionResponse result = transactionService.updateHikerStatus("id");

        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testGetTransactionByMonthAndYear() {
        SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("id")
                .build();

        Page<Transaction> transactions = new PageImpl<>(List.of(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build()));
        when(transactionRepository.getTransactionByMonthAndYearAndMountain(eq(1), eq(2020), eq("mountainId"),
                any(Pageable.class))).thenReturn(transactions);

        when(transactionMapper.entityToResponse(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build())).thenReturn(TransactionResponse.builder().build());

        Page<TransactionResponse> result = transactionService.getTransactionByMonthAndYear(1, 2020,
                "mountainId", searchRequest);

    }

    @Test
    void testGetByIdEntity() {
        Transaction expectedResult = Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build();

        Optional<Transaction> transaction = Optional.of(Transaction.builder()
                .hiker(Hiker.builder()
                        .ktp(Image.builder().build())
                        .build())
                .ranger(Ranger.builder().build())
                .mountain(Mountain.builder()
                        .id("id")
                        .price(new BigDecimal("0.00"))
                        .quotaLimit(0)
                        .build())
                .price(new BigDecimal("0.00"))
                .transactionDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)))
                .isUp(false)
                .isDown(false)
                .route(Route.builder().build())
                .payment(Payment.builder().build())
                .build());
        when(transactionRepository.findById("id")).thenReturn(transaction);

        Transaction result = transactionService.getByIdEntity("id");

        assertThat(result).isEqualTo(expectedResult);
    }
}
