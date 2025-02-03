package com.tanle.order_service.repo;

import com.tanle.order_service.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepo extends JpaRepository<OutBox,Integer> {
    @Query("from OutBox where isProcess = false")
    List<OutBox> findOutBoxesByNotProcess();
}
