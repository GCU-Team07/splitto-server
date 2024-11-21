package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.Payment;
import com.team7.spliito_server.model.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    List<PaymentDetail> findByPayment(Payment payment);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.payment.group.id = :groupId")
    List<PaymentDetail> findByGroupId(Long groupId);


}
