package com.robinhood.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleTradeNotFoundException() {

        // Arrange
        TradeNotFoundException exception =
                new TradeNotFoundException("Trade not found");

        // Act
        ResponseEntity<String> response =
                globalExceptionHandler.handleTradeNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trade not found", response.getBody());
    }

    @Test
    void testHandleInsufficientHoldingException() {

        // Arrange
        InsufficientHoldingException exception =
                new InsufficientHoldingException("Insufficient holding");

        // Act
        ResponseEntity<String> response =
                globalExceptionHandler.handleInsufficientHoldingException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient holding", response.getBody());
    }

    @Test
    void testHandleValidationErrors() {

        // Arrange
        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError =
                new FieldError(
                        "tradeRequest",
                        "quantity",
                        "Quantity must be positive"
                );

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<Map<String, String>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(
                "Quantity must be positive",
                response.getBody().get("quantity")
        );
    }
}