package com.robinhood.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.robinhood.entity.Trade;
import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;

import java.time.LocalDateTime;

@DataJpaTest
class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void testSaveTrade() {

        // ARRANGE
        Trade trade = new Trade();

        trade.setUserId(1L);
        trade.setStockSymbol("AAPL");
        trade.setTradeType(TradeType.BUY);
        trade.setQuantity(10);
        trade.setPrice(200.0);
        trade.setTotalAmount(2000.0);
        trade.setStatus(TradeStatus.COMPLETED);
        trade.setCreatedAt(LocalDateTime.now());

        // ACT
        Trade savedTrade =
                tradeRepository.save(trade);

        // ASSERT
        assertNotNull(savedTrade);
        assertNotNull(savedTrade.getId());

        assertEquals("AAPL", savedTrade.getStockSymbol());
        assertEquals(10, savedTrade.getQuantity());
    }
}