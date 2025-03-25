package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.TransactionStatus;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Payment;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RestClient restClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "SECRET_KEY", "U0ItTWlkLXNlcnZlci05Xy1oOElZUkp4ay1fZG05MDlzZ09IbHk6");
        ReflectionTestUtils.setField(paymentService, "PAYMENT_URL", "https://app.sandbox.midtrans.com/snap/v1/transactions");
        ReflectionTestUtils.setField(paymentService, "FETCH_URL", "https://api.sandbox.midtrans.com/v2/");
    }

    @Test
    void testCreatePayment() {
        Transaction transaction = Transaction.builder()
                .id("id")
                .hiker(Hiker.builder()
                        .name("firstName")
                        .email("email")
                        .phoneNumber("phone")
                        .build())
                .mountain(Mountain.builder()
                        .id("id")
                        .name("name")
                        .build())
                .price(new BigDecimal("0.00"))
                .payment(Payment.builder()
                        .id("id")
                        .token("token")
                        .redirectUrl("redirectUrl")
                        .transactionStatus(TransactionStatus.ORDERED)
                        .build())
                .build();
        Payment expectedResult = Payment.builder()
                .id("id")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus(TransactionStatus.ORDERED)
                .build();
        RestClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        when(restClient.post()).thenReturn(mockRequestBodyUriSpec);

        Payment payment = Payment.builder()
                .id("id")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus(TransactionStatus.ORDERED)
                .build();
        when(paymentRepository.save(Payment.builder()
                .id("id")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus(TransactionStatus.ORDERED)
                .build())).thenReturn(payment);

        Payment result = paymentService.createPayment(transaction);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdatePayment() {
        Transaction transaction = Transaction.builder()
                .id("id")
                .hiker(Hiker.builder()
                        .name("firstName")
                        .email("email")
                        .phoneNumber("phone")
                        .build())
                .mountain(Mountain.builder()
                        .id("id")
                        .name("name")
                        .build())
                .price(new BigDecimal("0.00"))
                .payment(Payment.builder()
                        .id("id")
                        .token("token")
                        .redirectUrl("redirectUrl")
                        .transactionStatus(TransactionStatus.ORDERED)
                        .build())
                .build();
        Map<String, String> expectedResult = Map.ofEntries(Map.entry("value", "value"));

        RestClient.RequestHeadersUriSpec<?> mockRequestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        doReturn(mockRequestHeadersUriSpec).when(restClient).get();

        Optional<Payment> payment = Optional.of(Payment.builder()
                .id("id")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus(TransactionStatus.ORDERED)
                .build());
        when(paymentRepository.findById("id")).thenReturn(payment);

        Map<String, String> result = paymentService.updatePayment(transaction);

        assertThat(result).isEqualTo(expectedResult);
        verify(paymentRepository).saveAndFlush(Payment.builder()
                .id("id")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus(TransactionStatus.ORDERED)
                .build());
    }
}
