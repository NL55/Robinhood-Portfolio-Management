package com.robinhood.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.robinhood.entity.Holding;
import com.robinhood.service.HoldingService;

@WebMvcTest(HoldingController.class)
public class Holdingcontrollertest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HoldingService holdingService;

    @Test
    void testGetHoldingsByUser() throws Exception {

        // ARRANGE
        Holding holding = new Holding();
        holding.setId(1L);
        holding.setUserId(100L);
        holding.setStockSymbol("AAPL");
        holding.setQuantity(50);

        when(holdingService.getHoldingsByUser(100L))
                .thenReturn(List.of(holding));

        // ACT + ASSERT
        mockMvc.perform(
                get("/api/holdings/user/100")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(100))
                .andExpect(jsonPath("$[0].stockSymbol").value("AAPL"))
                .andExpect(jsonPath("$[0].quantity").value(50));
    }

    @Test
    void testGetHolding() throws Exception {

        // ARRANGE
        Holding holding = new Holding();
        holding.setId(1L);
        holding.setUserId(100L);
        holding.setStockSymbol("AAPL");
        holding.setQuantity(50);

        when(holdingService.getHolding(100L, "AAPL"))
                .thenReturn(holding);

        // ACT + ASSERT
        mockMvc.perform(
                get("/api/holdings/user/100/AAPL")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.stockSymbol").value("AAPL"))
                .andExpect(jsonPath("$.quantity").value(50));
    }
}