package com.example.demo.service;

import com.example.demo.model.PriceCalculation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 价格计算服务测试
 *
 * @author demo
 */
@SpringBootTest
class PriceCalculationServiceTest {

    @Autowired
    private PriceCalculationService priceService;

    @Test
    void testCalculateFinalPrice() {
        PriceCalculation calc = new PriceCalculation(100.0, 2, 0.1, 10.0, 0.05);
        Double result = priceService.calculateFinalPrice(calc);
        
        // (100 * 2 * 0.9 + 10) * 1.05 = 199.5
        assertEquals(199.5, result, 0.01);
    }

    @Test
    void testCalculateDiscountPrice() {
        Double result = priceService.calculateDiscountPrice(100.0, 0.2);
        assertEquals(80.0, result, 0.01);
    }

    @Test
    void testCalculateVipDiscount() {
        Double result = priceService.calculateVipDiscount(100.0, 3);
        // 100 * (1 - 0.15) = 85.0
        assertEquals(85.0, result, 0.01);
    }

    @Test
    void testCalculateFullReduction() {
        assertEquals(90.0, priceService.calculateFullReduction(100.0), 0.01);
        assertEquals(175.0, priceService.calculateFullReduction(200.0), 0.01);
        assertEquals(420.0, priceService.calculateFullReduction(500.0), 0.01);
    }

    @Test
    void testCalculateTieredPrice() {
        Double result = priceService.calculateTieredPrice(60, 10.0);
        // 60 * 10 * 0.8 = 480.0
        assertEquals(480.0, result, 0.01);
    }

    @Test
    void testCalculateCompoundInterest() {
        Double result = priceService.calculateCompoundInterest(1000.0, 0.05, 12);
        // 1000 * 1.05^12 ≈ 1795.86
        assertTrue(result > 1795 && result < 1796);
    }
}

