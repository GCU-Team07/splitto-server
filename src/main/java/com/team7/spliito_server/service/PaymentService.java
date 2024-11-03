package com.team7.spliito_server.service;

import com.team7.spliito_server.dto.payment.AddPaymentRequest;
import com.team7.spliito_server.dto.payment.AddPaymentResponse;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.Payment;
import com.team7.spliito_server.model.PaymentDetail;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;


    @Transactional
    public AddPaymentResponse addPayment(Long groupId, AddPaymentRequest request, LocalDateTime now) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다!"));
        User paidUser = userRepository.findByUserNameAndGroupId(request.getPaidMember(), groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다!"));
        
        Payment payment = Payment.of(request.getItemName(), request.getItemPrice(), group, now);
        Payment savedPayment = paymentRepository.save(payment);
        
        Set<User> payUsers = getPayUsersWithNameAndGroupId(groupId, request);

        int eachPrice = calculateEachPrice(request, payUsers);
        savePaymentDetails(paidUser, savedPayment, payUsers, eachPrice);

        List<String> paidUserNames = payUsers.stream()
                .map(User::getName)
                .toList();

        return new AddPaymentResponse(savedPayment.getId(), eachPrice, paidUserNames);
    }

    private int calculateEachPrice(AddPaymentRequest request, Set<User> payUsers) {
        return request.getItemPrice() / payUsers.size();
    }

    private void savePaymentDetails(User paidUser, Payment savedPayment, Set<User> payUsers, int eachPrice) {
        payUsers.forEach(payUser -> {
            boolean isPayUser = !paidUser.getId().equals(payUser.getId());
            if (isPayUser) {
                PaymentDetail paymentDetail = PaymentDetail.of(payUser, paidUser, savedPayment, eachPrice);
                paymentDetailRepository.save(paymentDetail);
            }
        });
    }

    private Set<User> getPayUsersWithNameAndGroupId(Long groupId, AddPaymentRequest request) {
        return request.getPayMemberName().stream()
                .map(name -> userRepository.findByUserNameAndGroupId(name, groupId)
                        .orElseThrow(() -> new IllegalArgumentException("그룹에 존재하지 않는 유저입니다!")))
                .collect(Collectors.toSet());
    }
}
