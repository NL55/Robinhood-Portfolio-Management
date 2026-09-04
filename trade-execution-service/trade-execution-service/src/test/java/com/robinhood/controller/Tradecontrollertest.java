package com.robinhood.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.robinhood.dto.TradeRequest;
import com.robinhood.dto.TradeResponse;
import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;
import com.robinhood.service.TradeService;

@WebMvcTest(TradeController.class)
public class Tradecontrollertest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;


    @Test
    void testExecuteTradeSuccess() throws Exception {

        // ARRANGE
        TradeResponse response = new TradeResponse();
        response.setId(100L);
        response.setUserId(1L);
        response.setStockSymbol("AAPL");
        response.setTradeType(TradeType.BUY);
        response.setQuantity(10);
        response.setPrice(200.0);
        response.setTotalAmount(2000.0);
        response.setStatus(TradeStatus.COMPLETED);

        when(tradeService.executeTrade(any(TradeRequest.class)))
                .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                post("/api/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": 1,
                                    "stockSymbol": "AAPL",
                                    "tradeType": "BUY",
                                    "quantity": 10,
                                    "price": 200.0
                                }
                                """)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.stockSymbol").value("AAPL"))
                .andExpect(jsonPath("$.tradeType").value("BUY"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(200.0))
                .andExpect(jsonPath("$.totalAmount").value(2000.0))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }


    @Test
    void testExecuteTradeWithInvalidRequestShouldFail() throws Exception {

        // ACT + ASSERT
        mockMvc.perform(
                post("/api/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": 1,
                                    "stockSymbol": "",
                                    "tradeType": "BUY",
                                    "quantity": 10,
                                    "price": 200.0
                                }
                                """)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetTradeById() throws Exception {

        // ARRANGE
        TradeResponse response = new TradeResponse();
        response.setId(100L);
        response.setUserId(1L);
        response.setStockSymbol("AAPL");
        response.setTradeType(TradeType.BUY);
        response.setQuantity(10);
        response.setPrice(200.0);
        response.setTotalAmount(2000.0);
        response.setStatus(TradeStatus.COMPLETED);

        when(tradeService.getTradeById(100L))
                .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                get("/api/trades/100")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.stockSymbol").value("AAPL"));
    }


    @Test
    void testGetTradesByStockSymbol() throws Exception {

        // ARRANGE
        TradeResponse response = new TradeResponse();
        response.setId(100L);
        response.setStockSymbol("AAPL");

        when(tradeService.getTradesByStockSymbol("AAPL"))
                .thenReturn(List.of(response));

        // ACT + ASSERT
        mockMvc.perform(
                get("/api/trades/stock/AAPL")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].stockSymbol").value("AAPL"));
    }


    @Test
    void testGetTradesByUserId() throws Exception {

        // ARRANGE
        TradeResponse response = new TradeResponse();
        response.setId(100L);
        response.setUserId(1L);
        response.setStockSymbol("AAPL");

        when(tradeService.getTradesByUserId(1L))
                .thenReturn(List.of(response));

        // ACT + ASSERT
        mockMvc.perform(
                get("/api/trades/user/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].stockSymbol").value("AAPL"));
    }
}