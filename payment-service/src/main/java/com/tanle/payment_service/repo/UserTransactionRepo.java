package com.tanle.payment_service.repo;

import com.tanle.payment_service.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTransactionRepo extends JpaRepository<UserTransaction,Integer> {
}
