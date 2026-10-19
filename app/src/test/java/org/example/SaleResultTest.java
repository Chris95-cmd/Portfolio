package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SaleResultTest {
    @Test
    void constructorStoresAllValues() {
        SaleResult result = new SaleResult(30, 25, 2500, 4, 2);

        assertEquals(30, result.customers);
        assertEquals(25, result.cupsSold);
        assertEquals(2500, result.revenue);
        assertEquals(4, result.wastedPitchers);
        assertEquals(2, result.wastedCups);
    }

    @Test
    void constructorAllowsAllZeros() {
        SaleResult result = new SaleResult(0, 0, 0, 0, 0);

        assertEquals(0, result.customers);
        assertEquals(0, result.cupsSold);
        assertEquals(0, result.revenue);
        assertEquals(0, result.wastedPitchers);
        assertEquals(0, result.wastedCups);
    }
}
