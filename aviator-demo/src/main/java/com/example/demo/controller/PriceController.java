package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.PriceCalculation;
import com.example.demo.service.PriceCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 价格计算控制器
 *
 * @author demo
 */
@RestController
@RequestMapping("/api/price")
public class PriceController {

    @Autowired
    private PriceCalculationService priceService;

    /**
     * 计算最终价格
     * POST /api/price/final
     */
    @PostMapping("/final")
    public ApiResponse<Double> calculateFinalPrice(@RequestBody PriceCalculation calculation) {
        try {
            Double result = priceService.calculateFinalPrice(calculation);
            return ApiResponse.success("计算成功", result);
        } catch (Exception e) {
            return ApiResponse.error("计算失败: " + e.getMessage());
        }
    }

    /**
     * 计算折扣价格
     * GET /api/price/discount?price=100&rate=0.2
     */
    @GetMapping("/discount")
    public ApiResponse<Double> calculateDiscount(
            @RequestParam Double price,
            @RequestParam Double rate) {
        try {
            Double result = priceService.calculateDiscountPrice(price, rate);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算VIP折扣
     * GET /api/price/vip?price=100&level=3
     */
    @GetMapping("/vip")
    public ApiResponse<Double> calculateVipDiscount(
            @RequestParam Double price,
            @RequestParam Integer level) {
        try {
            Double result = priceService.calculateVipDiscount(price, level);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算满减优惠
     * GET /api/price/reduction?amount=250
     */
    @GetMapping("/reduction")
    public ApiResponse<Double> calculateFullReduction(@RequestParam Double amount) {
        try {
            Double result = priceService.calculateFullReduction(amount);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算阶梯价格
     * GET /api/price/tiered?quantity=60&unitPrice=10
     */
    @GetMapping("/tiered")
    public ApiResponse<Double> calculateTieredPrice(
            @RequestParam Integer quantity,
            @RequestParam Double unitPrice) {
        try {
            Double result = priceService.calculateTieredPrice(quantity, unitPrice);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算复利
     * GET /api/price/compound?principal=10000&rate=0.05&periods=12
     */
    @GetMapping("/compound")
    public ApiResponse<Double> calculateCompoundInterest(
            @RequestParam Double principal,
            @RequestParam Double rate,
            @RequestParam Integer periods) {
        try {
            Double result = priceService.calculateCompoundInterest(principal, rate, periods);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

