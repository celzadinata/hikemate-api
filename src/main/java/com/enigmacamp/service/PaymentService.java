package com.enigmacamp.service;

import com.enigmacamp.model.entity.Payment;
import com.enigmacamp.model.entity.Transaction;

import java.util.Map;

public interface PaymentService {
    Payment createPayment(Transaction transaction);
    Map<String, String> updatePayment(Transaction transaction);
}
