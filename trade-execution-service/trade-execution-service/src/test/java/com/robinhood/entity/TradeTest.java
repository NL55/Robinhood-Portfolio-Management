package com.robinhood.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;

public class TradeTest {

    @Test
    void testTradeFields() {

        // ARRANGE
        Trade trade = new Trade();

        trade.setUserId(1L);
        trade.setStockSymbol("AAPL");
        trade.setTradeType(TradeType.BUY);
        trade.setQuantity(10);
        trade.setPrice(200.0);
        trade.setTotalAmount(2000.0);
        trade.setStatus(TradeStatus.COMPLETED);

        // ACT
        Long userId = trade.getUserId();
        String stockSymbol = trade.getStockSymbol();
        TradeType tradeType = trade.getTradeType();
        Integer quantity = trade.getQuantity();
        Double price = trade.getPrice();
        Double totalAmount = trade.getTotalAmount();
        TradeStatus status = trade.getStatus();

        // ASSERT
        assertEquals(1L, userId);
        assertEquals("AAPL", stockSymbol);
        assertEquals(TradeType.BUY, tradeType);
        assertEquals(10, quantity);
        assertEquals(200.0, price);
        assertEquals(2000.0, totalAmount);
        assertEquals(TradeStatus.COMPLETED, status);
    }
     @Test
    void testTradeIdAndCreatedAt() {

        // ARRANGE
        Trade trade = new Trade();
        LocalDateTime createdTime = LocalDateTime.now();

        // ACT
        trade.setId(100L);
        trade.setCreatedAt(createdTime);

        // ASSERT
        assertEquals(100L, trade.getId());
        assertEquals(createdTime, trade.getCreatedAt());
    }
}