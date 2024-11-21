package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByGroupId(Long groupId);
}
