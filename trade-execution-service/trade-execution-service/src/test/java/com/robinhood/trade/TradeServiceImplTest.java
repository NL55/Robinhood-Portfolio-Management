package com.robinhood.trade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.robinhood.repository.TradeRepository;
import com.robinhood.repository.HoldingRepository;
import com.robinhood.service.TradeService;
import com.robinhood.dto.TradeRequest;
import com.robinhood.dto.TradeResponse;
import com.robinhood.enums.TradeType;
import com.robinhood.exception.InsufficientHoldingException;

@SpringBootTest
class TradeServiceImplTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    @BeforeEach
    void setUp() {
        tradeRepository.deleteAll();
        holdingRepository.deleteAll();
    }

    @Test
void testBuyTrade() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(120);
    request.setPrice(200.0);

    // ACT
    TradeResponse response =
            tradeService.executeTrade(request);

    // ASSERT 1 - CHECK RESPONSE
    assertNotNull(response);
    assertEquals(1L, response.getUserId());
    assertEquals("AAPL", response.getStockSymbol());
    assertEquals(TradeType.BUY, response.getTradeType());
    assertEquals(120, response.getQuantity());
    assertEquals(200.0, response.getPrice());

    // ASSERT 2 - CHECK DATABASE RECORD COUNT
    assertEquals(1, tradeRepository.count());
    assertEquals(1, holdingRepository.count());

    // ===== ADD NEW CODE HERE =====

    // ASSERT 3 - CHECK ACTUAL HOLDING DATA
    var holdings = holdingRepository.findAll();

    assertEquals(1, holdings.size());

    var holding = holdings.get(0);

    assertEquals(1L, holding.getUserId());
    assertEquals("AAPL", holding.getStockSymbol());
    assertEquals(120, holding.getQuantity());
}
  // ---------------------------------------------------------
    // TEST 2: Buying same stock again should increase Holding
    // ---------------------------------------------------------
    @Test
    void testBuySameStockShouldIncreaseHoldingQuantity() {

        // ARRANGE - FIRST BUY
        TradeRequest firstRequest = new TradeRequest();

        firstRequest.setUserId(1L);
        firstRequest.setStockSymbol("AAPL");
        firstRequest.setTradeType(TradeType.BUY);
        firstRequest.setQuantity(120);
        firstRequest.setPrice(200.0);

        tradeService.executeTrade(firstRequest);
         // ARRANGE - SECOND BUY
         TradeRequest secondRequest = new TradeRequest();

         secondRequest.setUserId(1L);
         secondRequest.setStockSymbol("AAPL");
         secondRequest.setTradeType(TradeType.BUY);
         secondRequest.setQuantity(30);
         secondRequest.setPrice(210.0);
 
         // ACT
         TradeResponse response =
                 tradeService.executeTrade(secondRequest);
 
         // ASSERT - SECOND TRADE RESPONSE
         assertNotNull(response);
 
         assertEquals(1L, response.getUserId());
         assertEquals("AAPL", response.getStockSymbol());
         assertEquals(TradeType.BUY, response.getTradeType());
         assertEquals(30, response.getQuantity());
         assertEquals(210.0, response.getPrice());
  // ASSERT - TWO TRADE RECORDS SHOULD EXIST
  assertEquals(2, tradeRepository.count());

  // ASSERT - ONLY ONE HOLDING SHOULD EXIST
  assertEquals(1, holdingRepository.count());

  // ASSERT - HOLDING QUANTITY SHOULD BE 120 + 30 = 150
  var holdings = holdingRepository.findAll();

  assertEquals(1, holdings.size());

  var holding = holdings.get(0);

  assertEquals(1L, holding.getUserId());
  assertEquals("AAPL", holding.getStockSymbol());
  assertEquals(150, holding.getQuantity());
} 
//sell trade should decrease holding quantity
@Test
void testSellTradeShouldDecreaseHoldingQuantity() {

    // ARRANGE - FIRST BUY 120 AAPL
    TradeRequest buyRequest = new TradeRequest();

    buyRequest.setUserId(1L);
    buyRequest.setStockSymbol("AAPL");
    buyRequest.setTradeType(TradeType.BUY);
    buyRequest.setQuantity(120);
    buyRequest.setPrice(200.0);

    tradeService.executeTrade(buyRequest);

    // ARRANGE - SELL 20 AAPL
    TradeRequest sellRequest = new TradeRequest();

    sellRequest.setUserId(1L);
    sellRequest.setStockSymbol("AAPL");
    sellRequest.setTradeType(TradeType.SELL);
    sellRequest.setQuantity(20);
    sellRequest.setPrice(210.0);

    // ACT
    TradeResponse response =
            tradeService.executeTrade(sellRequest);

    // ASSERT - RESPONSE
    assertNotNull(response);

    assertEquals(1L, response.getUserId());
    assertEquals("AAPL", response.getStockSymbol());
    assertEquals(TradeType.SELL, response.getTradeType());
    assertEquals(20, response.getQuantity());
    assertEquals(210.0, response.getPrice());

    // ASSERT - TWO TRADE RECORDS:
    // 1 BUY + 1 SELL
    assertEquals(2, tradeRepository.count());

    // ASSERT - STILL ONLY ONE AAPL HOLDING
    assertEquals(1, holdingRepository.count());

    // ASSERT - HOLDING QUANTITY:
    // 120 - 20 = 100
    var holdings = holdingRepository.findAll();

    assertEquals(1, holdings.size());

    var holding = holdings.get(0);

    assertEquals(1L, holding.getUserId());
    assertEquals("AAPL", holding.getStockSymbol());
    assertEquals(100, holding.getQuantity());
}
@Test
void testSellMoreThanAvailableQuantityShouldFail() {

    // ARRANGE - User first owns 120 AAPL
    TradeRequest buyRequest = new TradeRequest();

    buyRequest.setUserId(1L);
    buyRequest.setStockSymbol("AAPL");
    buyRequest.setTradeType(TradeType.BUY);
    buyRequest.setQuantity(120);
    buyRequest.setPrice(200.0);

    tradeService.executeTrade(buyRequest);

    // ARRANGE - Try to sell 150 AAPL
    TradeRequest sellRequest = new TradeRequest();

    sellRequest.setUserId(1L);
    sellRequest.setStockSymbol("AAPL");
    sellRequest.setTradeType(TradeType.SELL);
    sellRequest.setQuantity(150);
    sellRequest.setPrice(210.0);

    // ACT + ASSERT
    assertThrows(
            RuntimeException.class,
            () -> tradeService.executeTrade(sellRequest)
    );

    // ASSERT - Failed SELL should not create another trade
    assertEquals(1, tradeRepository.count());

    // ASSERT - Holding should remain unchanged
    assertEquals(1, holdingRepository.count());

    var holding = holdingRepository.findAll().get(0);

    assertEquals(120, holding.getQuantity());
}
//zero quantity trade should fail
@Test
void testBuyWithZeroQuantityShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(0);
    request.setPrice(200.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    // ASSERT - database should remain empty
    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
//negative quantity testing
@Test
void testBuyWithNegativeQuantityShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(-5);
    request.setPrice(200.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}

//zero price testing
@Test
void testBuyWithZeroPriceShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(10);
    request.setPrice(0.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
@Test
void testBuyWithNegativePriceShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(10);
    request.setPrice(-100.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    // ASSERT - NOTHING SHOULD BE SAVED
    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
@Test
void testBuyWithBlankStockSymbolShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("   ");
    request.setTradeType(TradeType.BUY);
    request.setQuantity(10);
    request.setPrice(200.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    // ASSERT - NOTHING SHOULD BE SAVED
    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
@Test
void testNullTradeTypeShouldFail() {

    // ARRANGE
    TradeRequest request = new TradeRequest();

    request.setUserId(1L);
    request.setStockSymbol("AAPL");
    request.setTradeType(null);
    request.setQuantity(10);
    request.setPrice(200.0);

    // ACT + ASSERT
    assertThrows(
            IllegalArgumentException.class,
            () -> tradeService.executeTrade(request)
    );

    // ASSERT - NOTHING SHOULD BE SAVED
    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
@Test
void testSellWithoutHoldingShouldFail() {

    // ARRANGE
    TradeRequest sellRequest = new TradeRequest();

    sellRequest.setUserId(1L);
    sellRequest.setStockSymbol("AAPL");
    sellRequest.setTradeType(TradeType.SELL);
    sellRequest.setQuantity(10);
    sellRequest.setPrice(200.0);

    // ACT + ASSERT
    assertThrows(
            InsufficientHoldingException.class,
            () -> tradeService.executeTrade(sellRequest)
    );

    // ASSERT - NOTHING SHOULD BE SAVED
    assertEquals(0, tradeRepository.count());
    assertEquals(0, holdingRepository.count());
}
}