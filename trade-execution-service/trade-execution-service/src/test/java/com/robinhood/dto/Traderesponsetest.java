package com.robinhood.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;

public class Traderesponsetest {

    @Test
    void testTradeResponseFields() {

        // ARRANGE
        TradeResponse response = new TradeResponse();
        LocalDateTime createdTime = LocalDateTime.now();

        // ACT
        response.setUserId(1L);
        response.setStockSymbol("AAPL");
        response.setTradeType(TradeType.BUY);
        response.setQuantity(10);
        response.setPrice(200.0);
        response.setTotalAmount(2000.0);
        response.setStatus(TradeStatus.COMPLETED);
        response.setCreatedAt(createdTime);

        // ASSERT
        assertEquals(1L, response.getUserId());
        assertEquals("AAPL", response.getStockSymbol());
        assertEquals(TradeType.BUY, response.getTradeType());
        assertEquals(10, response.getQuantity());
        assertEquals(200.0, response.getPrice());
        assertEquals(2000.0, response.getTotalAmount());
        assertEquals(TradeStatus.COMPLETED, response.getStatus());
        assertEquals(createdTime, response.getCreatedAt());
    }

    @Test
    void testTradeResponseId() {

        // ARRANGE
        TradeResponse response = new TradeResponse();

        // ACT
        response.setId(100L);

        // ASSERT
        assertEquals(100L, response.getId());
    }
}