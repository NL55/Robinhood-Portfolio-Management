package com.robinhood.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robinhood.entity.Holding;
import com.robinhood.service.HoldingService;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    // Returns all holdings for one user
    @GetMapping("/user/{userId}")
    public List<Holding> getHoldingsByUser(
            @PathVariable Long userId) {

        return holdingService.getHoldingsByUser(userId);
    }

    // Returns one specific stock holding
    @GetMapping("/user/{userId}/{stockSymbol}")
    public Holding getHolding(
            @PathVariable Long userId,
            @PathVariable String stockSymbol) {

        return holdingService.getHolding(userId, stockSymbol);
    }
}