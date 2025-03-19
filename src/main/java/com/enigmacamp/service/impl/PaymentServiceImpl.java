package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.TransactionStatus;
import com.enigmacamp.model.dto.request.payment.PaymentRequest;
import com.enigmacamp.model.dto.request.payment.advanced_request.*;
import com.enigmacamp.model.entity.Payment;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.repository.PaymentRepository;
import com.enigmacamp.service.PaymentService;
import com.enigmacamp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestClient restClient;

    @Value("${payment.secret.key}")
    private String SECRET_KEY;

    @Value("${payment.url}")
    private String PAYMENT_URL;

    @Value("${payment.fetch.url}")
    private String FETCH_URL;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Transaction transaction) {

        List<PaymentItemDetailsRequest> paymentItemDetailsRequests = List.of(new PaymentItemDetailsRequest(
                transaction.getMountain().getId(),
                transaction.getPrice().longValue(),
                1,
                "Tiket masuk " + transaction.getMountain().getName()));

        PaymentCustomerDetailRequest paymentCustomerDetailRequest = PaymentCustomerDetailRequest.builder()
                .firstName(transaction.getHiker().getName())
                .email(transaction.getHiker().getEmail())
                .phone(transaction.getHiker().getPhoneNumber())
                .build();

        PaymentPageExpiryRequest paymentPageExpiryRequest = PaymentPageExpiryRequest.builder()
                .duration(15)
                .unit("minutes")
                .build();

        PaymentShippingAddressRequest paymentShippingAddressRequest = PaymentShippingAddressRequest.builder()
                .firstName(paymentCustomerDetailRequest.getFirstName())
                .email(paymentCustomerDetailRequest.getEmail())
                .phone(paymentCustomerDetailRequest.getPhone())
                .build();;

        PaymentDetailRequest paymentDetailRequest = PaymentDetailRequest.builder()
                .orderId(transaction.getId())
                .grossAmount(transaction.getPrice().longValue())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .transactionDetails(paymentDetailRequest)
                .itemDetails(paymentItemDetailsRequests)
                .customerDetails(paymentCustomerDetailRequest)
                .pageExpiry(paymentPageExpiryRequest)
                .shippingAddress(paymentShippingAddressRequest)
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(PAYMENT_URL)
                .body(paymentRequest)
                .header("Authorization", "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> body = response.getBody();

        assert body != null;
        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus(TransactionStatus.ORDERED)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public Map<String, String> updatePayment(Transaction transaction) {
        ResponseEntity<Map<String, String>> response = restClient.get()
                .uri(FETCH_URL + transaction.getId() + "/status")
                .header("Authorization", "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                });
        Payment payment = paymentRepository.findById(transaction.getPayment().getId()).orElseThrow(() -> new RuntimeException("Payment not found!"));
        System.out.println(response.getBody());
        if (response.getBody().get("status_code").equals("404")) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment required to check payment status!");
        }
        payment.setTransactionStatus(TransactionStatus.valueOf(response.getBody().get("transaction_status").toUpperCase()));
        paymentRepository.saveAndFlush(payment);
        return response.getBody();
    }
}
