package com.robinhood.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.robinhood.enums.TradeType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class Traderequestvalidationtest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    private TradeRequest createValidRequest() {
        TradeRequest request = new TradeRequest();

        request.setUserId(1L);
        request.setStockSymbol("AAPL");
        request.setTradeType(TradeType.BUY);
        request.setQuantity(10);
        request.setPrice(200.0);

        return request;
    }

    @Test
    void testValidTradeRequest() {

        // ARRANGE
        TradeRequest request = createValidRequest();

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUserIdNullShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setUserId(null);

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertEquals(1, violations.size());
        assertEquals(
                "User ID is required",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void testStockSymbolBlankShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setStockSymbol("");

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getMessage().equals(
                                        "Stock Symbol is required"
                                ))
        );
    }

    @Test
    void testInvalidStockSymbolShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setStockSymbol("AAPL123");

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getMessage().equals(
                                        "Stock Symbol must contain only letters and be 1 to 10 characters"
                                ))
        );
    }

    @Test
    void testTradeTypeNullShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setTradeType(null);

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertEquals(1, violations.size());
        assertEquals(
                "Trade Type is required",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void testQuantityZeroShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setQuantity(0);

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertEquals(1, violations.size());
        assertEquals(
                "Quantity must be greater than zero",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void testPriceZeroShouldFail() {

        // ARRANGE
        TradeRequest request = createValidRequest();
        request.setPrice(0.0);

        // ACT
        Set<ConstraintViolation<TradeRequest>> violations =
                validator.validate(request);

        // ASSERT
        assertEquals(1, violations.size());
        assertEquals(
                "Price must be greater than zero",
                violations.iterator().next().getMessage()
        );
    }
}