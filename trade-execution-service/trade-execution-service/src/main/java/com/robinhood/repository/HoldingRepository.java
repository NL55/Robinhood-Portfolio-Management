package com.robinhood.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robinhood.entity.Holding;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    Optional<Holding> findByUserIdAndStockSymbol(
            Long userId,
            String stockSymbol
    );

    List<Holding> findByUserId(Long userId);
}