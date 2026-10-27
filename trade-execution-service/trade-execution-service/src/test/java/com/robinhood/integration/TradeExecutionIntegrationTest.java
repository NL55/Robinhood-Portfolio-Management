package com.robinhood.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.robinhood.dto.TradeRequest;
import com.robinhood.dto.TradeResponse;
import com.robinhood.enums.TradeType;
import com.robinhood.repository.HoldingRepository;
import com.robinhood.repository.TradeRepository;
import com.robinhood.service.TradeService;
@SpringBootTest
class TradeExecutionIntegrationTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    @BeforeEach
    void setUp() {

        // Arrange - clean database before every test
        tradeRepository.deleteAll();
        holdingRepository.deleteAll();
    }

    @Test
    void testExecuteBuyTradeIntegration() {

        // Arrange
        TradeRequest request = new TradeRequest();

        request.setUserId(1L);
        request.setStockSymbol("AAPL");
        request.setTradeType(TradeType.BUY);
        request.setQuantity(10);
        request.setPrice(150.00);

        // Act
        TradeResponse response = tradeService.executeTrade(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());

        assertEquals(1L, response.getUserId());
        assertEquals("AAPL", response.getStockSymbol());
        assertEquals(TradeType.BUY, response.getTradeType());
        assertEquals(10, response.getQuantity());
        assertEquals(150.00, response.getPrice(), 0.001);
        
    }
}