package com.robinhood.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.robinhood.entity.Trade;
import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;

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

        assertEquals(1L, savedTrade.getUserId());
        assertEquals("AAPL", savedTrade.getStockSymbol());
        assertEquals(TradeType.BUY, savedTrade.getTradeType());
        assertEquals(10, savedTrade.getQuantity());
        assertEquals(200.0, savedTrade.getPrice());
        assertEquals(2000.0, savedTrade.getTotalAmount());
        assertEquals(TradeStatus.COMPLETED, savedTrade.getStatus());
    }
    @Test
void testFindTradeById() {

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

    Trade savedTrade =
            tradeRepository.save(trade);

    Long tradeId = savedTrade.getId();

    // ACT
    var result =
            tradeRepository.findById(tradeId);

    // ASSERT
    assertTrue(result.isPresent());

    Trade foundTrade = result.get();

    assertEquals(tradeId, foundTrade.getId());
    assertEquals(1L, foundTrade.getUserId());
    assertEquals("AAPL", foundTrade.getStockSymbol());
    assertEquals(TradeType.BUY, foundTrade.getTradeType());
    assertEquals(10, foundTrade.getQuantity());
    assertEquals(200.0, foundTrade.getPrice());
}
@Test
void testFindTradesByUserId() {

    // ARRANGE - Trade 1 for User 1
    Trade trade1 = new Trade();

    trade1.setUserId(1L);
    trade1.setStockSymbol("AAPL");
    trade1.setTradeType(TradeType.BUY);
    trade1.setQuantity(10);
    trade1.setPrice(200.0);
    trade1.setTotalAmount(2000.0);
    trade1.setStatus(TradeStatus.COMPLETED);
    trade1.setCreatedAt(LocalDateTime.now());

    // ARRANGE - Trade 2 for User 1
    Trade trade2 = new Trade();

    trade2.setUserId(1L);
    trade2.setStockSymbol("TSLA");
    trade2.setTradeType(TradeType.BUY);
    trade2.setQuantity(5);
    trade2.setPrice(300.0);
    trade2.setTotalAmount(1500.0);
    trade2.setStatus(TradeStatus.COMPLETED);
    trade2.setCreatedAt(LocalDateTime.now());

    // ARRANGE - Trade for another user
    Trade trade3 = new Trade();

    trade3.setUserId(2L);
    trade3.setStockSymbol("MSFT");
    trade3.setTradeType(TradeType.BUY);
    trade3.setQuantity(8);
    trade3.setPrice(100.0);
    trade3.setTotalAmount(800.0);
    trade3.setStatus(TradeStatus.COMPLETED);
    trade3.setCreatedAt(LocalDateTime.now());

    tradeRepository.save(trade1);
    tradeRepository.save(trade2);
    tradeRepository.save(trade3);

    // ACT
    var trades =
            tradeRepository.findByUserId(1L);

    // ASSERT
    assertNotNull(trades);
    assertEquals(2, trades.size());

    assertTrue(
            trades.stream()
                    .allMatch(trade -> trade.getUserId().equals(1L))
    );
}
@Test
void testFindTradesByStockSymbolIgnoreCase() {

    // ARRANGE - Trade 1
    Trade trade1 = new Trade();

    trade1.setUserId(1L);
    trade1.setStockSymbol("AAPL");
    trade1.setTradeType(TradeType.BUY);
    trade1.setQuantity(10);
    trade1.setPrice(200.0);
    trade1.setTotalAmount(2000.0);
    trade1.setStatus(TradeStatus.COMPLETED);
    trade1.setCreatedAt(LocalDateTime.now());

    // ARRANGE - Trade 2
    Trade trade2 = new Trade();

    trade2.setUserId(2L);
    trade2.setStockSymbol("AAPL");
    trade2.setTradeType(TradeType.SELL);
    trade2.setQuantity(5);
    trade2.setPrice(210.0);
    trade2.setTotalAmount(1050.0);
    trade2.setStatus(TradeStatus.COMPLETED);
    trade2.setCreatedAt(LocalDateTime.now());

    // ARRANGE - Different stock
    Trade trade3 = new Trade();

    trade3.setUserId(3L);
    trade3.setStockSymbol("TSLA");
    trade3.setTradeType(TradeType.BUY);
    trade3.setQuantity(3);
    trade3.setPrice(300.0);
    trade3.setTotalAmount(900.0);
    trade3.setStatus(TradeStatus.COMPLETED);
    trade3.setCreatedAt(LocalDateTime.now());

    tradeRepository.save(trade1);
    tradeRepository.save(trade2);
    tradeRepository.save(trade3);

    // ACT
    var trades =
            tradeRepository.findByStockSymbolIgnoreCase("aapl");

    // ASSERT
    assertNotNull(trades);
    assertEquals(2, trades.size());

    assertTrue(
            trades.stream()
                    .allMatch(
                        trade ->
                            trade.getStockSymbol()
                                 .equalsIgnoreCase("AAPL")
                    )
    );
}
}