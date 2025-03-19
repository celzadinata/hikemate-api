package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.service.PaymentService;
import com.enigmacamp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = APIUrl.PAYMENT_API)
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/check-payment-status")
    public ResponseEntity<CommonResponse<Map<String, String>>> checkPaymentStatus(@RequestBody String transactionId){
        Transaction transaction = transactionService.getByIdEntity(transactionId);
        Map<String, String> paymentResponse = paymentService.updatePayment(transaction);

        CommonResponse<Map<String, String>> response = CommonResponse.<Map<String, String>>builder()
                .data(paymentResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
