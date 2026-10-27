package com.robinhood.service;

import java.util.List;

import com.robinhood.entity.Holding;

public interface HoldingService {

    Holding getHolding(Long userId, String stockSymbol);

    List<Holding> getHoldingsByUser(Long userId);
}