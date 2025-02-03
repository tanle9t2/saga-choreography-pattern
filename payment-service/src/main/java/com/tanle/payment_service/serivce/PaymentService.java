package com.tanle.payment_service.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.payment_service.dto.OrderRequestDto;
import com.tanle.payment_service.dto.PaymentRequestDto;
import com.tanle.payment_service.entity.OutBox;
import com.tanle.payment_service.entity.UserBalance;
import com.tanle.payment_service.entity.UserTransaction;
import com.tanle.payment_service.event.OrderEvent;
import com.tanle.payment_service.event.PaymentEvent;
import com.tanle.payment_service.event.PaymentStatus;
import com.tanle.payment_service.repo.OutBoxRepo;
import com.tanle.payment_service.repo.UserBalanceRepo;
import com.tanle.payment_service.repo.UserTransactionRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {
    @Autowired
    private UserTransactionRepo userTransactionRepo;
    @Autowired
    private UserBalanceRepo userBalanceRepo;
    @Autowired
    private OutBoxRepo outBoxRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initData() {
        userBalanceRepo.saveAll(Stream.of(new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)).collect(Collectors.toList()));
//        userTransactionRepo.save(new UserTransaction(1,1,1));
    }


    @Transactional  // Ensure all database operations are atomic
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(
                orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(),
                orderRequestDto.getAmount()
        );

        return userBalanceRepo.findById(orderRequestDto.getUserId())
                .filter(u -> u.getPrice() >= orderRequestDto.getAmount())  // Ensure balance is sufficient
                .map(u -> {
                    // Deduct the amount from user's balance
                    u.setPrice(u.getPrice() - orderRequestDto.getAmount());
                    userBalanceRepo.save(u);  // Save the updated balance
                    UserTransaction userTransaction = new UserTransaction(
                            orderRequestDto.getOrderId(),
                            orderRequestDto.getUserId(),
                            orderRequestDto.getAmount()
                    );
                    // Record the user transaction
                    userTransactionRepo.save(userTransaction);

                    // Create a payment event for the outbox
                    PaymentEvent paymentEvent = new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);

                    // Save to the Outbox for eventual consistency
                    OutBox outBox = OutBox.builder()
                            .payload(objectMapper.valueToTree(paymentEvent))
                            .aggregateId(String.valueOf(u.getUserId()))
                            .createdAt(new Date())
                            .isProcess(false)
                            .build();
                    outBoxRepo.save(outBox);  // Persist the outbox event

                    return paymentEvent;
                })
                .orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));  // Insufficient balance
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        userTransactionRepo.findById(orderEvent.getOrderRequestDto().getOrderId())
                .ifPresent(ut -> {
                    userTransactionRepo.delete(ut);
                    userBalanceRepo.findById(ut.getUserId())
                            .ifPresent(u -> u.setPrice(u.getPrice() + ut.getAmount()));
                });
    }
}
