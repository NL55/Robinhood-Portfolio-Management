package com.robinhood.dto;

import com.robinhood.enums.TradeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than zero")
    private Long userId;

    @NotBlank(message = "Stock Symbol is required")
@Pattern(
    regexp = "^[A-Za-z]{1,10}$",
    message = "Stock Symbol must contain only letters and be 1 to 10 characters"
)
private String stockSymbol;

    @NotNull(message = "Trade Type is required")
    private TradeType tradeType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
}