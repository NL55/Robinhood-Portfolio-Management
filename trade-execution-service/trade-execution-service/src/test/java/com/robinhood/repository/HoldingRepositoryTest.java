package com.robinhood.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.robinhood.entity.Holding;

@DataJpaTest
class HoldingRepositoryTest {

    @Autowired
    private HoldingRepository holdingRepository;

    @Test
    void testSaveHolding() {

        // ARRANGE
        Holding holding = new Holding();

        holding.setUserId(1L);
        holding.setStockSymbol("AAPL");
        holding.setQuantity(120);

        // ACT
        Holding savedHolding =
                holdingRepository.save(holding);

        // ASSERT
        assertNotNull(savedHolding);
        assertNotNull(savedHolding.getId());

        assertEquals(1L, savedHolding.getUserId());
        assertEquals("AAPL", savedHolding.getStockSymbol());
        assertEquals(120, savedHolding.getQuantity());
    }
    @Test
void testFindByUserIdAndStockSymbol() {

    // ARRANGE
    Holding holding = new Holding();

    holding.setUserId(1L);
    holding.setStockSymbol("AAPL");
    holding.setQuantity(120);

    holdingRepository.save(holding);

    // ACT
    var result =
            holdingRepository.findByUserIdAndStockSymbol(
                    1L,
                    "AAPL"
            );

    // ASSERT
    assertTrue(result.isPresent());

    Holding foundHolding = result.get();

    assertEquals(1L, foundHolding.getUserId());
    assertEquals("AAPL", foundHolding.getStockSymbol());
    assertEquals(120, foundHolding.getQuantity());
}
@Test
void testFindByUserIdAndStockSymbolNotFound() {

    // ARRANGE
    Holding holding = new Holding();

    holding.setUserId(1L);
    holding.setStockSymbol("AAPL");
    holding.setQuantity(120);

    holdingRepository.save(holding);

    // ACT
    var result =
            holdingRepository.findByUserIdAndStockSymbol(
                    1L,
                    "TSLA"
            );

    // ASSERT
    assertTrue(result.isEmpty());
}
}