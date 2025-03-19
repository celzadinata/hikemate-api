package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Payment;
import com.enigmacamp.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByTransaction(Transaction transaction);
}
