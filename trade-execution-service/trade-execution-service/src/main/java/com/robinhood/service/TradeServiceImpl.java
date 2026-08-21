package com.robinhood.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robinhood.dto.TradeRequest;
import com.robinhood.dto.TradeResponse;
import com.robinhood.entity.Holding;
import com.robinhood.entity.Trade;
import com.robinhood.enums.TradeStatus;
import com.robinhood.enums.TradeType;
import com.robinhood.exception.TradeNotFoundException;
import com.robinhood.repository.HoldingRepository;
import com.robinhood.repository.TradeRepository;
import com.robinhood.exception.InsufficientHoldingException;

@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;

    public TradeServiceImpl(
            TradeRepository tradeRepository,
            HoldingRepository holdingRepository) {

        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
    }

    @Override
    @Transactional
    public TradeResponse executeTrade(TradeRequest request) {
    
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    
        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }
    
        if (request.getStockSymbol() == null ||
                request.getStockSymbol().trim().isEmpty()) {
    
            throw new IllegalArgumentException(
                    "Stock symbol is required"
            );
        }
    
        String stockSymbol =
                request.getStockSymbol().trim().toUpperCase();

        Holding holding = holdingRepository
                .findByUserIdAndStockSymbol(
                        request.getUserId(),
                        stockSymbol
                )
                .orElse(null);

        if (request.getTradeType() == TradeType.BUY) {

            if (holding == null) {

                holding = new Holding();
                holding.setUserId(request.getUserId());
                holding.setStockSymbol(stockSymbol);
                holding.setQuantity(request.getQuantity());

            } else {

                int updatedQuantity =
                        holding.getQuantity() + request.getQuantity();

                holding.setQuantity(updatedQuantity);
            }

        } else if (request.getTradeType() == TradeType.SELL) {

            if (holding == null) {
                throw new InsufficientHoldingException(
                        "No holding available for stock: " + stockSymbol
                );
            }
        
            if (holding.getQuantity() < request.getQuantity()) {
                throw new InsufficientHoldingException(
                        "Insufficient quantity. Available: "
                                + holding.getQuantity()
                                + ", Requested: "
                                + request.getQuantity()
                );
            }
        
            int updatedQuantity =
                    holding.getQuantity() - request.getQuantity();
        
            holding.setQuantity(updatedQuantity);
        }else {
            throw new IllegalArgumentException(
                    "Unsupported trade type: " + request.getTradeType()
            );
        }

        holdingRepository.save(holding);

        Trade trade = new Trade();

        trade.setUserId(request.getUserId());
        trade.setStockSymbol(stockSymbol);
        trade.setTradeType(request.getTradeType());
        trade.setQuantity(request.getQuantity());
        trade.setPrice(request.getPrice());

        double totalAmount =
                request.getQuantity() * request.getPrice();

        trade.setTotalAmount(totalAmount);
        trade.setStatus(TradeStatus.COMPLETED);
        trade.setCreatedAt(LocalDateTime.now());

        Trade savedTrade = tradeRepository.save(trade);

        return convertToResponse(savedTrade);
    }

    @Override
    public TradeResponse getTradeById(Long id) {

        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() ->
                        new TradeNotFoundException(
                                "Trade not found with id: " + id
                        )
                );

        return convertToResponse(trade);
    }

    @Override
    public List<TradeResponse> getTradesByUserId(Long userId) {

        return tradeRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeResponse> getTradesByStockSymbol(String stockSymbol) {

        return tradeRepository
                .findByStockSymbolIgnoreCase(stockSymbol)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TradeResponse convertToResponse(Trade trade) {

        TradeResponse response = new TradeResponse();

        response.setId(trade.getId());
        response.setUserId(trade.getUserId());
        response.setStockSymbol(trade.getStockSymbol());
        response.setTradeType(trade.getTradeType());
        response.setQuantity(trade.getQuantity());
        response.setPrice(trade.getPrice());
        response.setTotalAmount(trade.getTotalAmount());
        response.setStatus(trade.getStatus());
        response.setCreatedAt(trade.getCreatedAt());

        return response;
    }
}