package com.robinhood.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.robinhood.entity.Holding;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // Find a specific stock holding for a specific user
    Optional<Holding> findByUserIdAndStockSymbol(
            Long userId,
            String stockSymbol
    );

    // Find all holdings for a specific user
    List<Holding> findByUserId(Long userId);
}