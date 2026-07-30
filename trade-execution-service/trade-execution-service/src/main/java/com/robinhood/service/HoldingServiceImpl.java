package com.robinhood.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.robinhood.entity.Holding;
import com.robinhood.repository.HoldingRepository;

@Service
public class HoldingServiceImpl implements HoldingService {

    private final HoldingRepository holdingRepository;

    public HoldingServiceImpl(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    @Override
public Holding getHolding(Long userId, String stockSymbol) {
    return holdingRepository
            .findByUserIdAndStockSymbol(
                    userId,
                    stockSymbol.toUpperCase()
            )
            .orElseThrow(() -> new RuntimeException("Holding not found"));
}

@Override
public List<Holding> getHoldingsByUser(Long userId) {
    return holdingRepository.findByUserId(userId);
}
}