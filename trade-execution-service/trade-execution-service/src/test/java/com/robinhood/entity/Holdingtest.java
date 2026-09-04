package com.robinhood.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Holdingtest {

    @Test
    void testHoldingFields() {

        // ARRANGE
        Holding holding = new Holding();

        holding.setUserId(1L);
        holding.setStockSymbol("AAPL");
        holding.setQuantity(100);

        // ACT
        Long userId = holding.getUserId();
        String stockSymbol = holding.getStockSymbol();
        Integer quantity = holding.getQuantity();

        // ASSERT
        assertEquals(1L, userId);
        assertEquals("AAPL", stockSymbol);
        assertEquals(100, quantity);
    }
    @Test
void testHoldingId() {

    // ARRANGE
    Holding holding = new Holding();

    // ACT
    holding.setId(100L);

    // ASSERT
    assertEquals(100L, holding.getId());
}
}