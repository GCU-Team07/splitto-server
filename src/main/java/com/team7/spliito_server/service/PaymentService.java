package com.team7.spliito_server.service;

import com.team7.spliito_server.dto.PaymentHistoryResponse;
import com.team7.spliito_server.dto.SettlementResponse;
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
import java.util.*;
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


//    public List<PaymentHistoryResponse> getPaymentHistory(Long groupId) {
//        List<Payment> payments = paymentRepository.findByGroupId(groupId);
//
//        return payments.stream()
//                .map(payment -> {
//                    List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPayment(payment);
//                    List<String> payMemberNames = paymentDetails.stream()
//                            .map(pd -> pd.getPayUser().getName())
//                            .collect(Collectors.toList());
//
//                    return new PaymentHistoryResponse(
//                            payment.getItemName(),
//                            payment.getTotalPrice()
//
//                    );
//                })
//                .collect(Collectors.toList());
//    }
    public List<SettlementResponse> getSettlement(Long groupId) {
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByGroupId(groupId);

        Map<String, Map<String, Integer>> balanceMap = new HashMap<>();

        for (PaymentDetail detail : paymentDetails) {
            String fromUser = detail.getPayUser().getName();
            String toUser = detail.getPaidUser().getName();
            int amount = detail.getPrice();

            // fromUser가 toUser에게 빚진 금액을 누적
            balanceMap.putIfAbsent(fromUser, new HashMap<>());
            balanceMap.get(fromUser).merge(toUser, amount, Integer::sum);
        }

        // 상호 간의 빚을 상쇄하여 최종 정산 금액 계산
        List<SettlementResponse> settlements = new ArrayList<>();
        Set<String> users = balanceMap.keySet();

        for (String userA : users) {
            Map<String, Integer> owesTo = balanceMap.get(userA);
            for (String userB : owesTo.keySet()) {
                int amountAB = owesTo.get(userB);
                int amountBA = balanceMap.getOrDefault(userB, Collections.emptyMap()).getOrDefault(userA, 0);
                int netAmount = amountAB - amountBA;

                if (netAmount > 0 && userA.compareTo(userB) < 0) {
                    settlements.add(new SettlementResponse(
                            userA + " → " + userB,
                            netAmount
                    ));
                } else if (netAmount < 0 && userB.compareTo(userA) < 0) {
                    settlements.add(new SettlementResponse(
                            userB + " → " + userA,
                            -netAmount
                    ));
                }
            }
        }

        return settlements;
    }







}
