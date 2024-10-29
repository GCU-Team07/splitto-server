package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
