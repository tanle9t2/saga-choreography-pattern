package com.tanle.payment_service.repo;

import com.tanle.payment_service.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceRepo extends JpaRepository<UserBalance,Integer> {
}
